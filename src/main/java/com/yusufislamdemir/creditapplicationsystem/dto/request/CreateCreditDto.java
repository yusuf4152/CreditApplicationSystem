package com.yusufislamdemir.creditapplicationsystem.dto.request;

import lombok.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCreditDto {

    @NotNull(message = "turkish republic identity number must be not null")
    @NotBlank(message = "turkish republic identity number must be not blank")
    @Digits(message = "turkish republic identity number must have 11 digits and must type integer", integer = 11, fraction = 0)
    private String userTc;
    private Long guarantee;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateCreditDto that = (CreateCreditDto) o;
        return Objects.equals(userTc, that.userTc) && Objects.equals(guarantee, that.guarantee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userTc, guarantee);
    }
}
