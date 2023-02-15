package com.yusufislamdemir.creditapplicationsystem.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    private String tcIdentityNumber;
    private String name;
    private String surname;
    private int monthlyIncome;
    private String phoneNumber;
    private Date dateOfBirth;
    private int creditScore;
    private Role role;
    private String password;
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "user")
    private List<Credit> creditList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return monthlyIncome == user.monthlyIncome && Objects.equals(tcIdentityNumber, user.tcIdentityNumber) && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(dateOfBirth, user.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tcIdentityNumber, name, surname, monthlyIncome, phoneNumber, dateOfBirth);
    }
}
