package com.yusufislamdemir.creditapplicationsystem.dto.response;


import com.yusufislamdemir.creditapplicationsystem.entity.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAccountDto {
    private long id;
    private GetUserDto user;
    private long currentWealth;
}
