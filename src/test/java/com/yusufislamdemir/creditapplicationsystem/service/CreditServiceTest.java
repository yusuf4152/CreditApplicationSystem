package com.yusufislamdemir.creditapplicationsystem.service;

import com.yusufislamdemir.creditapplicationsystem.dto.converter.GetCreditDtoConverter;
import com.yusufislamdemir.creditapplicationsystem.dto.request.CreateCreditDto;
import com.yusufislamdemir.creditapplicationsystem.dto.response.GetCreditDto;
import com.yusufislamdemir.creditapplicationsystem.dto.response.GetUserDto;
import com.yusufislamdemir.creditapplicationsystem.entity.Credit;
import com.yusufislamdemir.creditapplicationsystem.entity.Role;
import com.yusufislamdemir.creditapplicationsystem.entity.User;
import com.yusufislamdemir.creditapplicationsystem.exception.CreditMessage;
import com.yusufislamdemir.creditapplicationsystem.repository.CreditRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;


@ExtendWith(MockitoExtension.class)
class CreditServiceTest {
    @InjectMocks
    private CreditService creditService;
    @Mock
    private CreditRepository creditRepository;
    @Mock
    private UserService userService;
    @Mock
    private GetCreditDtoConverter getCreditDtoConverter;

    @Test
    void createCredit() {
        CreateCreditDto createCreditDto = CreateCreditDto.builder()
                .userTc("11111111111")
                .guarantee(1000L)
                .build();

        GetUserDto getUserDto = GetUserDto.builder()
                .id(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(8000)
                .creditScore(600)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();

        User user = User.builder()
                .id(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(8000)
                .password("123456789")
                .creditScore(600)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();

        GetCreditDto getCreditDto = GetCreditDto.builder()
                .message(CreditMessage.CONFIRMATION)
                .limit(22000)
                .guarantee(1000L)
                .user(getUserDto)
                .build();

        Credit credit = Credit.builder()
                .resultMessage(CreditMessage.CONFIRMATION)
                .limit(22000)
                .guarantee(1000L)
                .user(user)
                .build();

        when(creditRepository.save(credit)).thenReturn(credit);
        when(userService.getUserByTcIdentityNumber(createCreditDto.getUserTc())).thenReturn(user);
        when(getCreditDtoConverter.convert(credit)).thenReturn(getCreditDto);

        GetCreditDto result = creditService.createCredit(createCreditDto);

        verify(creditRepository).save(credit);
        verify(getCreditDtoConverter).convert(credit);
        then(result).isEqualTo(getCreditDto);
    }

    @Test
    void getCreditWhenCreditExist() {
        GetUserDto getUserDto = GetUserDto.builder()
                .id(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(8000)
                .creditScore(600)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();

        User user = User.builder()
                .id(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(8000)
                .password("123456789")
                .creditScore(600)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();

        GetCreditDto getCreditDto = GetCreditDto.builder()
                .message(CreditMessage.CONFIRMATION)
                .limit(22000)
                .guarantee(1000L)
                .user(getUserDto)
                .build();

        Credit credit = Credit.builder()
                .resultMessage(CreditMessage.CONFIRMATION)
                .limit(22000)
                .guarantee(1000L)
                .user(user)
                .build();
        when(creditRepository.findByUserIdentificationNumberAndBirthDate(user.getTcIdentityNumber(), user.getDateOfBirth())).thenReturn(Optional.of(credit));
        when(getCreditDtoConverter.convert(credit)).thenReturn(getCreditDto);
        GetCreditDto result = creditService.getCredit(user.getTcIdentityNumber(), user.getDateOfBirth());

        verify(creditRepository).findByUserIdentificationNumberAndBirthDate(user.getTcIdentityNumber(), user.getDateOfBirth());
        verify(getCreditDtoConverter).convert(credit);
        then(result).isEqualTo(getCreditDto);
    }

    @Test
    void getCreditWhenCreditNotExist() {
        User user = User.builder()
                .id(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(8000)
                .password("123456789")
                .creditScore(600)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        //when
        when(creditRepository.findByUserIdentificationNumberAndBirthDate(user.getTcIdentityNumber(), user.getDateOfBirth())).thenReturn(Optional.empty());
        EntityNotFoundException result = assertThrows(EntityNotFoundException.class, () -> creditService.getCredit(user.getTcIdentityNumber(), user.getDateOfBirth()));
        //then
        verify(creditRepository).findByUserIdentificationNumberAndBirthDate(user.getTcIdentityNumber(), user.getDateOfBirth());
        then(result.getMessage()).isEqualTo("Credit not found");
    }
}