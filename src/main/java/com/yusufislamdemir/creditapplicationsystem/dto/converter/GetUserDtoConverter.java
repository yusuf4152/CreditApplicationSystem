package com.yusufislamdemir.creditapplicationsystem.dto.converter;

import com.yusufislamdemir.creditapplicationsystem.dto.response.GetUserDto;
import com.yusufislamdemir.creditapplicationsystem.entity.User;
import org.springframework.stereotype.Component;

@Component
public class GetUserDtoConverter {
    public GetUserDto convert(User from) {
        return GetUserDto.builder()
                .id(from.getId())
                .role(from.getRole())
                .name(from.getName())
                .surname(from.getSurname())
                .tcIdentityNumber(from.getTcIdentityNumber())
                .phoneNumber(from.getPhoneNumber())
                .monthlyIncome(from.getMonthlyIncome())
                .dateOfBirth(from.getDateOfBirth())
                .creditScore(from.getCreditScore())
                .isDeleted(from.isDeleted())
                .build();
    }
}
