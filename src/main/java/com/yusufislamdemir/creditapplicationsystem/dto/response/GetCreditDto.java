package com.yusufislamdemir.creditapplicationsystem.dto.response;

import lombok.*;

import java.util.Objects;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCreditDto {
    private long id;
    private long limit;
    private long guarantee;
    private GetUserDto user;
    private String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetCreditDto that = (GetCreditDto) o;
        return guarantee == that.guarantee && Objects.equals(user, that.user) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guarantee, user, message);
    }
}
