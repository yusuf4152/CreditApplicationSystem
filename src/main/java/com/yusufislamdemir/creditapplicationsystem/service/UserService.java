package com.yusufislamdemir.creditapplicationsystem.service;

import com.yusufislamdemir.creditapplicationsystem.dto.converter.GetUserDtoConverter;
import com.yusufislamdemir.creditapplicationsystem.dto.request.CreateUserDto;
import com.yusufislamdemir.creditapplicationsystem.dto.request.UpdateUserDto;
import com.yusufislamdemir.creditapplicationsystem.dto.response.GetUserDto;
import com.yusufislamdemir.creditapplicationsystem.entity.Role;
import com.yusufislamdemir.creditapplicationsystem.entity.User;
import com.yusufislamdemir.creditapplicationsystem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private final GetUserDtoConverter getUserDtoConverter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, GetUserDtoConverter getUserDtoConverter, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.getUserDtoConverter = getUserDtoConverter;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public GetUserDto getUserById(long id) {
        User user = userRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> {
            log.error("user not found for id=" + "id");
            return new EntityNotFoundException("user not found for id=" + id);
        });
        return getUserDtoConverter.convert(user);
    }

    private User checkUserById(long id) {
        return userRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> {
            log.error("user not found");
            return new EntityNotFoundException("user not found for id=" + id);
        });
    }

    public GetUserDto createUser(CreateUserDto createUserDto) {
        Random random = new Random();
        User user = User.builder()
                .name(createUserDto.getName())
                .dateOfBirth(createUserDto.getDateOfBirth())
                .role(Role.ROLE_USER)
                .phoneNumber(createUserDto.getPhoneNumber())
                .password(bCryptPasswordEncoder.encode(createUserDto.getPassword()))
                .surname(createUserDto.getSurname())
                .monthlyIncome(createUserDto.getMonthlyIncome())
                .tcIdentityNumber(createUserDto.getTcIdentityNumber())
                .creditScore(random.nextInt(1001))
                .build();
        User checkUser = getUserByTcIdentityNumber(user.getTcIdentityNumber());
        if (checkUser != null) {
            throw new IllegalArgumentException("user identity number must not be duplicate");
        }
        return getUserDtoConverter.convert(userRepository.save(user));

    }

    public GetUserDto giveAdminRoleToUser(long userId) {
        User user = checkUserById(userId);
        user.setRole(Role.ROLE_ADMIN);
        return getUserDtoConverter.convert(userRepository.save(user));
    }

    public GetUserDto UpdateUser(UpdateUserDto updateUserDto) {
        User databaseUser = checkUserById(updateUserDto.getUserId());
        User user = User.builder()
                .name(updateUserDto.getName())
                .dateOfBirth(updateUserDto.getDateOfBirth())
                .role(databaseUser.getRole())
                .phoneNumber(updateUserDto.getPhoneNumber())
                .surname(updateUserDto.getSurname())
                .monthlyIncome(updateUserDto.getMonthlyIncome())
                .tcIdentityNumber(updateUserDto.getTcIdentityNumber())
                .build();
        return getUserDtoConverter.convert(userRepository.save(user));
    }

    public List<GetUserDto> getAllUsers() {
        return userRepository
                .findAllByIsDeletedFalse()
                .stream()
                .map(getUserDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public GetUserDto deleteUserById(long id) {
        User user = checkUserById(id);
        user.setDeleted(true);
        return getUserDtoConverter.convert(userRepository.save(user));
    }

    public User getUserByTcIdentityNumber(String tcIdentityNumber) {

        return userRepository.findByTcIdentityNumberAndIsDeletedFalse(tcIdentityNumber)
                .orElseThrow(() -> {
                    log.info("user not found");
                    return new EntityNotFoundException("user not found");
                });
    }

    public GetUserDto getUserDtoByTcIdentityNumber(String tcIdentityNumber) {
        return getUserDtoConverter.convert(getUserByTcIdentityNumber(tcIdentityNumber));
    }
}
