package com.evil.learning.bootifulacid.service;

import com.evil.learning.bootifulacid.domain.User;
import com.evil.learning.bootifulacid.domain.Wallet;
import com.evil.learning.bootifulacid.repo.UserRepository;
import com.evil.learning.bootifulacid.repo.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void transfer(String fromUsername, String toUsername, double amount) {
        Wallet fromWallet = walletRepository.findByUser_UserName(fromUsername);
        Wallet toWallet = walletRepository.findByUser_UserName(toUsername);

        log.info("Entering transaction");
        if (fromWallet.getBalance() >= amount) {
            heavyStuff();
            walletRepository.updateBalance(fromWallet.getId(), -amount);
            walletRepository.updateBalance(toWallet.getId(), amount);
            log.info("Transaction succeeded");
        } else {
            log.info("Transaction failed. Insufficient funds for " + fromUsername);
        }
    }

    @Override
    @Transactional
    public void inMemoryTransfer(String fromUsername, String toUsername, double amount) {
        Wallet fromWallet = walletRepository.findByUser_UserName(fromUsername);
        Wallet toWallet = walletRepository.findByUser_UserName(toUsername);

        log.info("Entering transaction");
        if (fromWallet.getBalance() >= amount) {
            heavyStuff();
            fromWallet.withdraw(amount);
            walletRepository.save(fromWallet);
            toWallet.deposit(amount);
            walletRepository.save(toWallet);
            log.info("Transaction succeeded");
        } else {
            log.info("Transaction failed. Insufficient funds for " + fromUsername);
        }
    }

    private void heavyStuff() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
