package com.yusufislamdemir.creditapplicationsystem;

import com.yusufislamdemir.creditapplicationsystem.entity.Role;
import com.yusufislamdemir.creditapplicationsystem.entity.User;
import com.yusufislamdemir.creditapplicationsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Optional;

@SpringBootApplication
public class CreditApplicationSystemApplication {

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {

        SpringApplication.run(CreditApplicationSystemApplication.class, args);
    }

    // I wrote it so that a user with the admin role can be used when it will be tested
    @PostConstruct
    public void init() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = User.builder()
                .phoneNumber("1111111111")
                .role(Role.ROLE_ADMIN)
                .monthlyIncome(15000)
                .password(bCryptPasswordEncoder.encode("123456789"))
                .creditScore(700)
                .dateOfBirth(LocalDate.of(2001, 3, 2))
                .tcIdentityNumber("98745632113")
                .name("yusuf")
                .surname("demir")
                .isDeleted(false)
                .build();
        if (userRepository.findByTcIdentityNumberAndIsDeletedFalse(user.getTcIdentityNumber()).isEmpty()) {
            userRepository.save(user);
        }
    }
}
