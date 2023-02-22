package com.yusufislamdemir.creditapplicationsystem.service;

import com.yusufislamdemir.creditapplicationsystem.dto.converter.GetCreditDtoConverter;
import com.yusufislamdemir.creditapplicationsystem.dto.request.CreateCreditDto;
import com.yusufislamdemir.creditapplicationsystem.dto.response.GetCreditDto;
import com.yusufislamdemir.creditapplicationsystem.entity.Credit;
import com.yusufislamdemir.creditapplicationsystem.entity.User;
import com.yusufislamdemir.creditapplicationsystem.exception.CreditMessage;
import com.yusufislamdemir.creditapplicationsystem.repository.CreditRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Date;

@Service
@Slf4j
public class CreditService {
    private final UserService userService;
    private final CreditRepository creditRepository;
    private final GetCreditDtoConverter getCreditDtoConverter;

    @Value("${credit.limit.multiplier}")
    private int creditLimitMultiplier;

    public CreditService(UserService userService, CreditRepository creditRepository, GetCreditDtoConverter getCreditDtoConverter) {
        this.userService = userService;
        this.creditRepository = creditRepository;
        this.getCreditDtoConverter = getCreditDtoConverter;
    }

    public GetCreditDto createCredit(CreateCreditDto createCreditDto) {
        User user = userService.getUserByTcIdentityNumber(createCreditDto.getUserTc());
        int creditScore = user.getCreditScore();
        Long guarantee = createCreditDto.getGuarantee() != null ? createCreditDto.getGuarantee() : 0;
        if (creditScore < 500) {
            log.warn("credit not created beacuse credit score less than");
            sendSms(user.getPhoneNumber(), CreditMessage.REFUSE);
            return getCreditDtoConverter
                    .convert(generateCredit(0, guarantee, user, CreditMessage.REFUSE));
        }
        if (creditScore >= 500 && creditScore <= 1000 && user.getMonthlyIncome() < 5000) {

            log.info("credit created");
            long limit = 20000 + ((guarantee * 10) / 100);
            sendSms(user.getPhoneNumber(), CreditMessage.CONFIRMATION);
            return getCreditDtoConverter
                    .convert(creditRepository.save(generateCredit(limit, guarantee, user, CreditMessage.CONFIRMATION)));
        }
        if (creditScore >= 500 && creditScore <= 1000 && user.getMonthlyIncome() >= 5000 && user.getMonthlyIncome() < 10000) {
            log.info("credit created");
            long limit = 20000 + ((guarantee * 20) / 100);
            sendSms(user.getPhoneNumber(), CreditMessage.CONFIRMATION);
            return getCreditDtoConverter
                    .convert(creditRepository.save(generateCredit(limit, guarantee, user, CreditMessage.CONFIRMATION)));
        }
        if (creditScore >= 500 && creditScore <= 1000 && user.getMonthlyIncome() >= 10000) {

            log.info("credit created");
            sendSms(user.getPhoneNumber(), CreditMessage.CONFIRMATION);
            long limit = (long) user.getMonthlyIncome() * creditLimitMultiplier + ((guarantee * 25) / 100);
            return getCreditDtoConverter
                    .convert(creditRepository.save(generateCredit(limit, guarantee, user, CreditMessage.CONFIRMATION)));
        }
        sendSms(user.getPhoneNumber(), CreditMessage.CONFIRMATION);
        long limit = (long) user.getMonthlyIncome() * creditLimitMultiplier + ((guarantee * 50) / 100);
        return getCreditDtoConverter
                .convert(creditRepository.save(generateCredit(limit, guarantee, user, CreditMessage.CONFIRMATION)));
    }

    private Credit generateCredit(long limit, Long guarantee, User user, String message) {
        return Credit.builder()
                .limit(limit)
                .guarantee(guarantee)
                .resultMessage(message)
                .user(user)
                .build();
    }

    private void sendSms(String phoneNumber, String message) {
        log.info(phoneNumber + " " + message);
    }

    public GetCreditDto getCredit(String userTc, LocalDate dateOfBirth) {
        Credit credit = creditRepository.findByUserIdentificationNumberAndBirthDate(userTc, dateOfBirth)
                .orElseThrow(() -> {
                            log.error("credit not found for tc "+userTc+" dateofbirth "+dateOfBirth);
                            return new EntityNotFoundException("Credit not found");
                        }
                );
        log.info("credit get");
        return getCreditDtoConverter.convert(credit);
    }
}
