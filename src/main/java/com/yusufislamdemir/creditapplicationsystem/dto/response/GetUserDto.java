package com.yusufislamdemir.creditapplicationsystem.dto.response;

import com.yusufislamdemir.creditapplicationsystem.entity.Role;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetUserDto implements Serializable {
    private long id;
    private String tcIdentityNumber;
    private String name;
    private String surname;
    private int monthlyIncome;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private int creditScore;
    private Role role;
    private boolean isDeleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetUserDto that = (GetUserDto) o;
        return monthlyIncome == that.monthlyIncome && Objects.equals(tcIdentityNumber, that.tcIdentityNumber) && Objects.equals(name, that.name) && Objects.equals(surname, that.surname) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(dateOfBirth, that.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tcIdentityNumber, name, surname, monthlyIncome, phoneNumber, dateOfBirth);
    }
}
