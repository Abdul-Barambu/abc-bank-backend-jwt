package com.abdul.abcbank.bank.repository;

import com.abdul.abcbank.bank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);
    Boolean existsByAccountNumber(String accountNumber);
    Boolean existsByName(String name);
    User findByAccountNumber(String accountNumber);
    User findByName(String name);

}
