package com.yusufislamdemir.creditapplicationsystem.dto.request;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequestDto {
    private String tcIdentityNumber;
    private String password;
}
