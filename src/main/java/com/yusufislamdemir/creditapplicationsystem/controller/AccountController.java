package com.yusufislamdemir.creditapplicationsystem.controller;

import com.yusufislamdemir.creditapplicationsystem.dto.request.CreateAccountDto;
import com.yusufislamdemir.creditapplicationsystem.dto.request.TransferMoneyRequest;
import com.yusufislamdemir.creditapplicationsystem.dto.response.GetAccountDto;
import com.yusufislamdemir.creditapplicationsystem.service.AccountService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @PostMapping
    public ResponseEntity<GetAccountDto> createAccount(@RequestBody CreateAccountDto createAccountDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(createAccountDto));
    }

    @GetMapping
    public ResponseEntity<List<GetAccountDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAllAccounts());
    }

    @PutMapping("/addMoney/{accountId}/{money}")
    public ResponseEntity<GetAccountDto> addMoney(@PathVariable long accountId, @PathVariable long money) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.addMoney(accountId, money));
    }

    @PutMapping("/withDrawMoney/{accountId}/{money}")
    public ResponseEntity<GetAccountDto> withDrawMoney(@PathVariable long accountId, @PathVariable long money) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.withDrawMoney(accountId, money));
    }

    @PutMapping("/transferMoney")
    public ResponseEntity<String> transferMoney(@RequestBody TransferMoneyRequest transferMoneyRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.transferMoney(transferMoneyRequest));
    }


}
