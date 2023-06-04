package com.yusufislamdemir.creditapplicationsystem.service;

import com.yusufislamdemir.creditapplicationsystem.dto.converter.GetAccountDtoConverter;
import com.yusufislamdemir.creditapplicationsystem.dto.request.CreateAccountDto;
import com.yusufislamdemir.creditapplicationsystem.dto.request.TransferMoneyRequest;
import com.yusufislamdemir.creditapplicationsystem.dto.response.GetAccountDto;
import com.yusufislamdemir.creditapplicationsystem.entity.Account;
import com.yusufislamdemir.creditapplicationsystem.repository.AccountRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final GetAccountDtoConverter getAccountDtoConverter;
    private final DirectExchange exchange;

    private final AmqpTemplate rabbitTemplate;


    @Value("${sample.rabbitmq.routingKey}")
    String routingKey;

    @Value("${sample.rabbitmq.queue}")
    String queueName;

    public AccountService(AccountRepository accountRepository, UserService userService, GetAccountDtoConverter getAccountDtoConverter, DirectExchange exchange, AmqpTemplate rabbitTemplate) {
        this.accountRepository = accountRepository;
        this.userService = userService;
        this.getAccountDtoConverter = getAccountDtoConverter;
        this.exchange = exchange;
        this.rabbitTemplate = rabbitTemplate;
    }

    public GetAccountDto createAccount(CreateAccountDto createAccountDto) {
        Account account = Account.builder()
                .CurrentWealth((int) (Math.random() * 1000 + 1))
                .user(userService.checkUserById(createAccountDto.getUserId()))
                .build();
        return getAccountDtoConverter.convert(accountRepository.save(account));
    }

    public List<GetAccountDto> getAllAccounts() {
        return accountRepository.findAll().stream().map(getAccountDtoConverter::convert).collect(Collectors.toList());
    }

    public GetAccountDto addMoney(long accountId, long money) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("account not found for id= " + accountId));
        account.setCurrentWealth(account.getCurrentWealth() + money);
        return getAccountDtoConverter.convert(accountRepository.save(account));
    }

    public GetAccountDto withDrawMoney(long accountId, long money) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("account not found for id= " + accountId));
        if (account.getCurrentWealth() < money) {
            throw new IllegalArgumentException("Insufficient funds current balance= " + account.getCurrentWealth());
        }
        account.setCurrentWealth(account.getCurrentWealth() - money);
        return getAccountDtoConverter.convert(accountRepository.save(account));
    }

    public String transferMoney(TransferMoneyRequest transferMoneyRequest) {
        rabbitTemplate.convertAndSend(exchange.getName(), routingKey, transferMoneyRequest);
        return "istek başarıyla işleme alınmıştır";
    }

    @RabbitListener(queues = "firstStepQueue")
    public void withDrawSender(TransferMoneyRequest transferMoneyRequest) {
        accountRepository.findById(transferMoneyRequest.getSenderAccountId()).ifPresentOrElse(
                senderAccount -> {
                    if (senderAccount.getCurrentWealth() > transferMoneyRequest.getAmount()) {
                        senderAccount.setCurrentWealth(senderAccount.getCurrentWealth() - transferMoneyRequest.getAmount());
                        accountRepository.save(senderAccount);
                        rabbitTemplate.convertAndSend(exchange.getName(), "secondRoute", transferMoneyRequest);
                    } else {
                        System.out.println("Insufficient funds for accountId=" + transferMoneyRequest.getSenderAccountId()
                                + " currentWealth=" + senderAccount.getCurrentWealth());
                    }
                },
                () -> System.out.println("senderAccount not found for id= " + transferMoneyRequest.getSenderAccountId())
        );
    }

    @RabbitListener(queues = "secondStepQueue")
    public void addMoneyReceiver(TransferMoneyRequest transferMoneyRequest) {
        accountRepository.findById(transferMoneyRequest.getReceiverAccountId()).ifPresentOrElse(
                receiverAccount -> {
                    receiverAccount.setCurrentWealth(receiverAccount.getCurrentWealth() + transferMoneyRequest.getAmount());
                    accountRepository.save(receiverAccount);
                    rabbitTemplate.convertAndSend(exchange.getName(), "thirdRoute", transferMoneyRequest);
                },
                () -> {
                    System.out.println("receiver Account not found for id= " + transferMoneyRequest.getSenderAccountId());
                    String deneme = "receiver Account not found for id=" + transferMoneyRequest.getSenderAccountId();
                    Optional<Account> senderAccount = accountRepository.findById(transferMoneyRequest.getSenderAccountId());
                    senderAccount.ifPresent(
                            account -> {
                                account.setCurrentWealth(account.getCurrentWealth() + transferMoneyRequest.getAmount());
                                accountRepository.save(account);
                                System.out.println("the money is retransferred to the sender account");
                            }
                    );
                }
        );
    }

    @RabbitListener(queues = "thirdStepQueue")
    public void finalizeTransfer(TransferMoneyRequest transferMoneyRequest) {
        accountRepository.findById(transferMoneyRequest.getSenderAccountId())
                .ifPresentOrElse(
                        account -> {
                            System.out.println(" sender account id=" + transferMoneyRequest.getSenderAccountId() +
                                    " withDrawing money amount is=" + transferMoneyRequest.getAmount() +
                                    " sender account currentWealth=" + account.getCurrentWealth()
                            );
                        },
                        () -> {
                            System.out.println("sender account not found");
                        }
                );
        accountRepository.findById(transferMoneyRequest.getReceiverAccountId()).ifPresentOrElse(
                account -> {
                    System.out.println("receiver account id=" + transferMoneyRequest.getSenderAccountId() +
                            "added money amount is= " + transferMoneyRequest.getAmount() +
                            "receiver account currentWealth=" + account.getCurrentWealth()
                    );
                },
                () -> {
                    System.out.println("receiver account not found");
                }
        );
    }
}
