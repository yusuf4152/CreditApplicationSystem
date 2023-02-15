package com.yusufislamdemir.creditapplicationsystem.service;

import com.yusufislamdemir.creditapplicationsystem.dto.converter.GetUserDtoConverter;
import com.yusufislamdemir.creditapplicationsystem.dto.request.CreateUserDto;
import com.yusufislamdemir.creditapplicationsystem.dto.request.UpdateUserDto;
import com.yusufislamdemir.creditapplicationsystem.dto.response.GetUserDto;
import com.yusufislamdemir.creditapplicationsystem.entity.Role;
import com.yusufislamdemir.creditapplicationsystem.entity.User;
import com.yusufislamdemir.creditapplicationsystem.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
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
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("user not found for id=" + id));
        return getUserDtoConverter.convert(user);
    }

    private User checkUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("user not found for id=" + id));
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
                .orElseThrow(() -> new EntityNotFoundException("user not found"));
    }

    public GetUserDto getUserDtoByTcIdentityNumber(String tcIdentityNumber) {
        return getUserDtoConverter.convert(getUserByTcIdentityNumber(tcIdentityNumber));
    }
}
