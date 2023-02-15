package com.yusufislamdemir.creditapplicationsystem.dto.response;


import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDto {
    private String token;
    private GetUserDto userDto;
}
