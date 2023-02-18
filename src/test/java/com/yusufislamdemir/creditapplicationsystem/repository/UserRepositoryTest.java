package com.yusufislamdemir.creditapplicationsystem.repository;

import com.yusufislamdemir.creditapplicationsystem.entity.Role;
import com.yusufislamdemir.creditapplicationsystem.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@DataJpaTest
@Testcontainers
public class UserRepositoryTest {

    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
            .withUsername("test")
            .withDatabaseName("test")
            .withPassword("test");

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUserIdentificationNumberAndBirthDate() {
        // Test verileri oluşturma
        User user = User.builder()
                .id(1L)
                .name("yusuf")
                .tcIdentityNumber("11111111111")
                .role(Role.ROLE_USER)
                .password("123456789")
                .monthlyIncome(20000)
                .phoneNumber("2222222222")
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        userRepository.save(user);
        Optional<User> result = userRepository.findByTcIdentityNumberAndIsDeletedFalse("11111111111");

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getName()).isEqualTo("yusuf");
    }

    @TestConfiguration
    static class MySQLTestContainersConfiguration {
        @Bean
        public static MySQLContainer<?> mySQLContainer() {
            return mySQLContainer;
        }
    }
}
