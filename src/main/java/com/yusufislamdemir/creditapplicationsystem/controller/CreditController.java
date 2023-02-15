package com.yusufislamdemir.creditapplicationsystem.controller;

import com.yusufislamdemir.creditapplicationsystem.dto.request.CreateCreditDto;
import com.yusufislamdemir.creditapplicationsystem.dto.response.GetCreditDto;
import com.yusufislamdemir.creditapplicationsystem.service.CreditService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/credit")
public class CreditController {
    private final CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @PostMapping("/createCredit")
    public ResponseEntity<GetCreditDto> createCredit(@Valid @RequestBody CreateCreditDto credit) {
        return ResponseEntity.status(HttpStatus.CREATED).body(creditService.createCredit(credit));
    }

    @GetMapping("/getCredit")
    public ResponseEntity<GetCreditDto> getCredit(@RequestParam String userTc, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOfBirth) {
        return ResponseEntity.status(HttpStatus.OK).body(creditService.getCredit(userTc, dateOfBirth));
    }
}
