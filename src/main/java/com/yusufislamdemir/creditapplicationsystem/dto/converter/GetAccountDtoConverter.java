package com.yusufislamdemir.creditapplicationsystem.dto.converter;

import com.yusufislamdemir.creditapplicationsystem.dto.response.GetAccountDto;
import com.yusufislamdemir.creditapplicationsystem.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class GetAccountDtoConverter {

    private final GetUserDtoConverter getUserDtoConverter;

    public GetAccountDtoConverter(GetUserDtoConverter getUserDtoConverter) {
        this.getUserDtoConverter = getUserDtoConverter;
    }

    public GetAccountDto convert(Account from) {
        return GetAccountDto.builder()
                .id(from.getId())
                .currentWealth(from.getCurrentWealth())
                .user(getUserDtoConverter.convert(from.getUser()))
                .build();
    }
}

