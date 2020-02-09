package com.evil.learning.bootifulacid.service;

import com.evil.learning.bootifulacid.domain.User;
import com.evil.learning.bootifulacid.domain.Wallet;
import com.evil.learning.bootifulacid.repo.UserRepository;
import com.evil.learning.bootifulacid.repo.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class WalletServiceImplTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private WalletService walletService;

    private User dennis;
    private User alice;
    private User bob;

    public static final int THREAD_COUNT = 4;


    @BeforeEach
    void setUp() {

        userRepository.deleteAll();
        walletRepository.deleteAll();

        dennis = new User("whoami", "dennis", "ritchie", LocalDate.of(1941, Month.SEPTEMBER, 9));
        alice = new User("alice37", "alice", "wonderland", LocalDate.of(1999, Month.OCTOBER, 9));
        bob = new User("bob69", "bob", "dylan", LocalDate.of(1999, Month.OCTOBER, 9));
        Wallet dennisWallet = new Wallet();
        dennis.setWallet(dennisWallet);
        dennisWallet.setUser(dennis);

        Wallet aliceWallet = new Wallet();
        alice.setWallet(aliceWallet);
        aliceWallet.setUser(alice);
        aliceWallet.setBalance(280L);

        Wallet bobWallet = new Wallet();
        bob.setWallet(bobWallet);
        bobWallet.setUser(bob);
        bobWallet.setBalance(5000L);

        userRepository.saveAll(Arrays.asList(alice, dennis, bob));
        walletRepository.saveAll(Arrays.asList(aliceWallet, dennisWallet, bobWallet));

        log.info(userRepository.findByUserName(dennis.getUserName()).toString());
        log.info(userRepository.findByUserName(alice.getUserName()).toString());
        log.info(userRepository.findByUserName(bob.getUserName()).toString());
    }

    @Test
    public void transfer_inSerialExecution_shouldWorkFine() {
        walletService.transfer(alice.getUserName(), dennis.getUserName(), 200);

        log.info(walletRepository.findByUser(alice).toString());
        log.info(walletRepository.findByUser(dennis).toString());

        walletService.inMemoryTransfer(alice.getUserName(), dennis.getUserName(), 200);

        log.info(walletRepository.findByUser(alice).toString());
        log.info(walletRepository.findByUser(dennis).toString());

        assertThat(walletRepository.findByUser(alice).getBalance()).isEqualTo(80.0);
        assertThat(walletRepository.findByUser(dennis).getBalance()).isEqualTo(200.0);
    }

    @Test
    public void transfer_inParallel_shouldWorkFine() {
        CyclicBarrier gate = new CyclicBarrier(THREAD_COUNT + 1);

        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(() -> {
                awaitOnGate(gate);
                walletService.inMemoryTransfer(alice.getUserName(), dennis.getUserName(), 200);
            }).start();
        }
        awaitOnGate(gate);
        waitForThreads();

        log.info(walletRepository.findByUser(alice).toString());
        log.info(walletRepository.findByUser(dennis).toString());

        assertThat(walletRepository.findByUser(alice).getBalance()).isEqualTo(80.0);
        assertThat(walletRepository.findByUser(dennis).getBalance()).isEqualTo(200.0);
    }

    private void waitForThreads() {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void awaitOnGate(CyclicBarrier gate) {
        try {
            gate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}