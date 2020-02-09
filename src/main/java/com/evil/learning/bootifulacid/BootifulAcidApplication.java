package com.evil.learning.bootifulacid;

import com.evil.learning.bootifulacid.domain.User;
import com.evil.learning.bootifulacid.domain.Wallet;
import com.evil.learning.bootifulacid.repo.UserRepository;
import com.evil.learning.bootifulacid.repo.WalletRepository;
import com.evil.learning.bootifulacid.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Example;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

@SpringBootApplication
@Slf4j
public class BootifulAcidApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootifulAcidApplication.class, args);
	}

}
