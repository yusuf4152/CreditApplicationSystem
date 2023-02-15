package com.yusufislamdemir.creditapplicationsystem.repository;


import com.yusufislamdemir.creditapplicationsystem.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface CreditRepository extends JpaRepository<Credit, String> {

    @Query("SELECT c FROM Credit c INNER JOIN c.user u WHERE u.tcIdentityNumber = :identificationNumber AND DATE(u.dateOfBirth) = :birthDate")
   Optional <Credit> findByUserIdentificationNumberAndBirthDate(@Param("identificationNumber") String identificationNumber,
                                                              @Param("birthDate") Date birthDate);

}
