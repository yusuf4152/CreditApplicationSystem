package com.yusufislamdemir.creditapplicationsystem.dto.converter;

import com.yusufislamdemir.creditapplicationsystem.dto.response.GetCreditDto;
import com.yusufislamdemir.creditapplicationsystem.entity.Credit;
import org.springframework.stereotype.Component;

@Component
public class GetCreditDtoConverter {
    private final GetUserDtoConverter getUserDtoConverter;

    public GetCreditDtoConverter(GetUserDtoConverter getUserDtoConverter) {
        this.getUserDtoConverter = getUserDtoConverter;
    }

    public GetCreditDto convert(Credit from) {
        return GetCreditDto.builder()
                .id(from.getId())
                .guarantee(from.getGuarantee())
                .limit(from.getLimit())
                .user(getUserDtoConverter.convert(from.getUser()))
                .message(from.getResultMessage())
                .build();
    }

}
