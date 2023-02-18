package com.yusufislamdemir.creditapplicationsystem.service;

import com.yusufislamdemir.creditapplicationsystem.dto.converter.GetUserDtoConverter;
import com.yusufislamdemir.creditapplicationsystem.dto.request.CreateUserDto;
import com.yusufislamdemir.creditapplicationsystem.dto.request.UpdateUserDto;
import com.yusufislamdemir.creditapplicationsystem.dto.response.GetUserDto;
import com.yusufislamdemir.creditapplicationsystem.entity.Role;
import com.yusufislamdemir.creditapplicationsystem.entity.User;
import com.yusufislamdemir.creditapplicationsystem.repository.UserRepository;
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

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GetUserDtoConverter getUserDtoConverter;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void getUserById() {
        long userId = 2;
        User user = User.builder()
                .id(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .password("123456789")
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .role(Role.ROLE_ADMIN)
                .build();
        GetUserDto getUserDto = GetUserDto.builder()
                .id(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .role(Role.ROLE_ADMIN)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        //when
        when(userRepository.findByIdAndIsDeletedFalse(userId)).thenReturn(Optional.of(user));
        when(getUserDtoConverter.convert(user)).thenReturn(getUserDto);
        //then
        GetUserDto result = userService.getUserById(userId);
        verify(userRepository).findByIdAndIsDeletedFalse(userId);
        verify(getUserDtoConverter).convert(user);
        then(result).isEqualTo(getUserDto);
    }

    @Test
    void createUser() {
        //given
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .password("123456789")
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        User user = User.builder()
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .password("123456789")
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        GetUserDto getUserDto = GetUserDto.builder()
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        //when
        when(userRepository.save(user)).thenReturn(user);
        when(getUserDtoConverter.convert(user)).thenReturn(getUserDto);
        GetUserDto result = userService.createUser(createUserDto);
        //then
        Mockito.verify(userRepository).save(user);
        Mockito.verify(getUserDtoConverter).convert(user);
        then(result).isEqualTo(getUserDto);
    }

    @Test
    void giveAdminRoleToUserWhenUserExist() {
        long userId = 2;
        User user = User.builder()
                .id(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .password("123456789")
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .role(Role.ROLE_ADMIN)
                .build();
        GetUserDto getUserDto = GetUserDto.builder()
                .id(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .role(Role.ROLE_ADMIN)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        //when
        when(userRepository.findByIdAndIsDeletedFalse(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(getUserDtoConverter.convert(user)).thenReturn(getUserDto);
        //then
        GetUserDto result = userService.giveAdminRoleToUser(userId);
        verify(userRepository).findByIdAndIsDeletedFalse(userId);
        verify(getUserDtoConverter).convert(user);
        then(result).isEqualTo(getUserDto);
    }

    @Test
    void giveAdminRoleToUserWhenUserNotExist() {
        long userId = 2;
        String message = "user not found for id=" + userId;
        //when
        when(userRepository.findByIdAndIsDeletedFalse(userId)).thenReturn(Optional.empty());
        EntityNotFoundException result = assertThrows(EntityNotFoundException.class, () -> userService.giveAdminRoleToUser(userId));
        //then
        verify(userRepository).findByIdAndIsDeletedFalse(userId);
        then(result.getMessage()).isEqualTo(message);
    }


    @Test
    void updateUserWhenUserExist() {
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .UserId(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        User user = User.builder()
                .id(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .password("123456789")
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        GetUserDto getUserDto = GetUserDto.builder()
                .id(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        //when
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByIdAndIsDeletedFalse(updateUserDto.getUserId())).thenReturn(Optional.of(user));
        when(getUserDtoConverter.convert(user)).thenReturn(getUserDto);
        GetUserDto result = userService.UpdateUser(updateUserDto);
        //then
        Mockito.verify(userRepository).save(user);
        Mockito.verify(getUserDtoConverter).convert(user);
        then(result).isEqualTo(getUserDto);
    }

    @Test
    void updateUserWhenUserNotExist() {

        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .UserId(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        long userId = updateUserDto.getUserId();
        String message = "user not found for id=" + userId;
        //when
        when(userRepository.findByIdAndIsDeletedFalse(updateUserDto.getUserId())).thenReturn(Optional.empty());
        EntityNotFoundException result = assertThrows(EntityNotFoundException.class, () -> userService.UpdateUser(updateUserDto));
        //then
        verify(userRepository).findByIdAndIsDeletedFalse(updateUserDto.getUserId());
        then(result.getMessage()).isEqualTo(message);
    }

    @Test
    void getAllUsers() {
        User user = User.builder()
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .password("123456789")
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        List<User> users = new ArrayList<>();
        users.add(user);
        GetUserDto getUserDto = GetUserDto.builder()
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        List<GetUserDto> getUserDtos = new ArrayList<>();
        getUserDtos.add(getUserDto);
        when(userRepository.findAllByIsDeletedFalse()).thenReturn(users);
        when(getUserDtoConverter.convert(user)).thenReturn(getUserDto);
        List<GetUserDto> results = userService.getAllUsers();
        then(results).isEqualTo(getUserDtos);
    }

    @Test
    void deleteUserByIdWhenUserExist() {
        long userId = 2;
        User user = User.builder()
                .id(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .password("123456789")
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .role(Role.ROLE_ADMIN)
                .isDeleted(true)
                .build();
        GetUserDto getUserDto = GetUserDto.builder()
                .id(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .role(Role.ROLE_ADMIN)
                .isDeleted(true)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        //when
        when(userRepository.findByIdAndIsDeletedFalse(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(getUserDtoConverter.convert(user)).thenReturn(getUserDto);
        //then
        GetUserDto result = userService.deleteUserById(userId);
        verify(userRepository).findByIdAndIsDeletedFalse(userId);
        verify(getUserDtoConverter).convert(user);
        then(result).isEqualTo(getUserDto);
    }

    @Test
    void deleteUserByIdWhenUserNotExist() {
        long userId = 2;
        String message = "user not found for id=" + userId;
        //when
        when(userRepository.findByIdAndIsDeletedFalse(userId)).thenReturn(Optional.empty());
        EntityNotFoundException result = assertThrows(EntityNotFoundException.class, () -> userService.deleteUserById(userId));
        //then
        verify(userRepository).findByIdAndIsDeletedFalse(userId);
        then(result.getMessage()).isEqualTo(message);
    }

    @Test
    void getUserDtoByTcIdentityNumberWhenUserExist() {
        String identityNumber = "11111111111";
        User user = User.builder()
                .id(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .password("123456789")
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .role(Role.ROLE_ADMIN)
                .build();
        GetUserDto getUserDto = GetUserDto.builder()
                .id(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .role(Role.ROLE_ADMIN)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        when(userRepository.findByTcIdentityNumberAndIsDeletedFalse(identityNumber)).thenReturn(Optional.of(user));
        when(getUserDtoConverter.convert(user)).thenReturn(getUserDto);
        GetUserDto result = userService.getUserDtoByTcIdentityNumber(identityNumber);
        verify(userRepository).findByTcIdentityNumberAndIsDeletedFalse(identityNumber);
        verify(getUserDtoConverter).convert(user);
        then(result).isEqualTo(getUserDto);
    }

    @Test
    void getUserDtoByTcIdentityNumberWhenUserNotExist() {
        String identityNumber = "11111111111";
        String message = "user not found";
        //when
        when(userRepository.findByTcIdentityNumberAndIsDeletedFalse(identityNumber)).thenReturn(Optional.empty());
        EntityNotFoundException result = assertThrows(EntityNotFoundException.class, () -> userService.getUserDtoByTcIdentityNumber(identityNumber));
        //then
        verify(userRepository).findByTcIdentityNumberAndIsDeletedFalse(identityNumber);
        then(result.getMessage()).isEqualTo(message);
    }

}