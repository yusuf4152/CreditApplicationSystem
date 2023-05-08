package com.yusufislamdemir.creditapplicationsystem.repository;

import com.yusufislamdemir.creditapplicationsystem.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
