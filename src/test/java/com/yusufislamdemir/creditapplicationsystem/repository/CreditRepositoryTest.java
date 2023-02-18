package com.yusufislamdemir.creditapplicationsystem.repository;

import com.yusufislamdemir.creditapplicationsystem.entity.Credit;
import com.yusufislamdemir.creditapplicationsystem.entity.Role;
import com.yusufislamdemir.creditapplicationsystem.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
class CreditRepositoryTest {

    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
            .withUsername("test")
            .withPassword("test")
            .withDatabaseName("test");

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void creditFindByUserIdentificationNumberAndBirthDate() {
        User user = User.builder()
                .id(1L)
                .name("yusuf")
                .tcIdentityNumber("11111111111")
                .role(Role.ROLE_USER)
                .password("123456789")
                .monthlyIncome(20000)
                .phoneNumber("2222222222")
                .dateOfBirth(LocalDate.of(2022,2,2))
                .build();
        User savedUser = userRepository.save(user);

        Credit credit = Credit.builder()
                .id(1L)
                .resultMessage("message")
                .limit(20000)
                .guarantee(5000L)
                .user(savedUser)
                .build();
        creditRepository.save(credit);
        Optional<Credit> result = creditRepository.findByUserIdentificationNumberAndBirthDate(savedUser.getTcIdentityNumber(), savedUser.getDateOfBirth());

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getResultMessage()).isEqualTo("message");
        assertThat(result.get().getUser().getTcIdentityNumber()).isEqualTo(user.getTcIdentityNumber());
    }

    @TestConfiguration
    static class MySQLTestContainersConfiguration {
        @Bean
        public static MySQLContainer<?> mySQLContainer() {
            return mySQLContainer;
        }
    }
}