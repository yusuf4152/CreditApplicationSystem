package com.yusufislamdemir.creditapplicationsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yusufislamdemir.creditapplicationsystem.dto.request.CreateCreditDto;
import com.yusufislamdemir.creditapplicationsystem.dto.response.GetCreditDto;
import com.yusufislamdemir.creditapplicationsystem.dto.response.GetUserDto;
import com.yusufislamdemir.creditapplicationsystem.exception.CreditMessage;
import com.yusufislamdemir.creditapplicationsystem.service.CreditService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class CreditControllerTest {

    @MockBean
    private CreditService creditService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCredit() throws Exception {
        CreateCreditDto createCreditDto = CreateCreditDto.builder()
                .userTc("11111111111")
                .guarantee(1000L)
                .build();

        GetUserDto getUserDto = GetUserDto.builder()
                .id(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(8000)
                .creditScore(600)
                .dateOfBirth(LocalDate.of(2022,2,2))
                .build();

        GetCreditDto getCreditDto = GetCreditDto.builder()
                .message(CreditMessage.CONFIRMATION)
                .limit(22000)
                .guarantee(1000L)
                .user(getUserDto)
                .build();

        when(creditService.createCredit(createCreditDto)).thenReturn(getCreditDto);
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/credit/createCredit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCreditDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        verify(creditService).createCredit(eq(createCreditDto));
        then(result).isEqualTo(objectMapper.writeValueAsString(getCreditDto));
    }
}