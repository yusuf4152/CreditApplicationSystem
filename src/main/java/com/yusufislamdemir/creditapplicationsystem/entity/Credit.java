package com.yusufislamdemir.creditapplicationsystem.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Long guarantee;
    private long limit;
    @ManyToOne
    private User user;
    private String resultMessage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credit credit = (Credit) o;
        return Objects.equals(guarantee, credit.guarantee) && Objects.equals(user, credit.user) && Objects.equals(resultMessage, credit.resultMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guarantee, user, resultMessage);
    }
}
