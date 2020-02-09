package com.evil.learning.bootifulacid.repo;

import com.evil.learning.bootifulacid.domain.User;
import com.evil.learning.bootifulacid.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
}
