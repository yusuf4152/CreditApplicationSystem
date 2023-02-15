package com.yusufislamdemir.creditapplicationsystem.repository;

import com.yusufislamdemir.creditapplicationsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findAllByIsDeletedFalse();
   Optional <User> findByTcIdentityNumberAndIsDeletedFalse(String tcIdentityNumber);

}
