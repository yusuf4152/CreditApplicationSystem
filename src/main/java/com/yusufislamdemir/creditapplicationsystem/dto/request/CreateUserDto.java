package com.yusufislamdemir.creditapplicationsystem.dto.request;


import lombok.*;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateUserDto {
    @NotNull(message = "turkish republic identity number must be not null")
    @NotBlank(message = "turkish republic identity number must be not blank")
    @Digits(message = "turkish republic identity number must have 11 digits and must type integer", integer = 11, fraction = 0)
    private String tcIdentityNumber;
    @NotNull(message = "name must be not null")
    @NotBlank(message = "name must be not blank")
    @Size(min = 3, max = 100, message = "name must be range of 3 to 100")
    private String name;
    @NotNull(message = "surname must be not null")
    @NotBlank(message = "surname must be not blank")
    @Size(min = 3, max = 100, message = "surname must be range of 3 to 100")
    private String surname;
    @NotNull(message = "monthlyIncome must be not null")
    @Min(value = 0, message = "monthlyIncome must be grater than zero")
    private int monthlyIncome;
    @NotNull(message = "phone number must be not null")
    @NotBlank(message = "phone number must be not blank")
    @Digits(message = "phone number must have 10 digits and must type integer", integer = 10, fraction = 0)
    private String phoneNumber;
    @NotNull(message = "date of birth must be not null")
    private Date dateOfBirth;
    @NotNull(message = "password must be not null")
    @NotBlank(message = "password must be not blank")
    @Min(value = 6, message = "password must be greater than six")
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateUserDto that = (CreateUserDto) o;
        return monthlyIncome == that.monthlyIncome && Objects.equals(tcIdentityNumber, that.tcIdentityNumber) && Objects.equals(name, that.name) && Objects.equals(surname, that.surname) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(dateOfBirth, that.dateOfBirth) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tcIdentityNumber, name, surname, monthlyIncome, phoneNumber, dateOfBirth, password);
    }
}
