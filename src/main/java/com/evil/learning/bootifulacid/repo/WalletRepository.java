package com.evil.learning.bootifulacid.repo;

import com.evil.learning.bootifulacid.domain.User;
import com.evil.learning.bootifulacid.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByUser_UserName(String userName);

    Wallet findByUser(User user);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE wallet set balance = balance + :amount where id = :id")
    void updateBalance(long id, double amount);
}
