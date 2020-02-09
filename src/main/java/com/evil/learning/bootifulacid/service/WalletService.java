package com.evil.learning.bootifulacid.service;

import org.springframework.transaction.annotation.Transactional;

public interface WalletService {
    void transfer(String fromUsername, String toUsername, double amount);

    void inMemoryTransfer(String fromUsername, String toUsername, double amount);
}
