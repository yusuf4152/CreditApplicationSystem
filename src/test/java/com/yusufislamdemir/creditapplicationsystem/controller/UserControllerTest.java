package com.yusufislamdemir.creditapplicationsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yusufislamdemir.creditapplicationsystem.dto.request.CreateUserDto;
import com.yusufislamdemir.creditapplicationsystem.dto.request.UpdateUserDto;
import com.yusufislamdemir.creditapplicationsystem.dto.response.GetUserDto;
import com.yusufislamdemir.creditapplicationsystem.entity.Role;
import com.yusufislamdemir.creditapplicationsystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void createUser() throws Exception {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .password("123456789")
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        GetUserDto getUserDto = GetUserDto.builder()
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        //when
        when(userService.createUser(createUserDto)).thenReturn(getUserDto);
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/user/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        verify(userService).createUser(eq(createUserDto));
        then(result).isEqualTo(objectMapper.writeValueAsString(getUserDto));
    }

    @Test
    void getUserById() throws Exception {
        long userId = 2;
        GetUserDto getUserDto = GetUserDto.builder()
                .id(userId)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        when(userService.getUserById(userId)).thenReturn(getUserDto);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/user/getUserById?id=2")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        verify(userService).getUserById(userId);
        then(result).isEqualTo(objectMapper.writeValueAsString(getUserDto));

    }

    @Test
    @WithMockUser(username = "11111111111", authorities = "ROLE_ADMIN")
    void giveAdminRoleToUser() throws Exception {
        long userId = 2;
        GetUserDto getUserDto = GetUserDto.builder()
                .id(userId)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .role(Role.ROLE_ADMIN)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        when(userService.giveAdminRoleToUser(userId)).thenReturn(getUserDto);

        MvcResult mvcResult = mockMvc.perform(patch("/api/v1/user/giveAdminRoleToUser?userId=2")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        verify(userService).giveAdminRoleToUser(userId);
        then(result).isEqualTo(objectMapper.writeValueAsString(getUserDto));
    }

    @Test
    @WithMockUser(username = "11111111111", authorities = "ROLE_ADMIN")
    void getAllUsers() throws Exception {
        List<GetUserDto> getUserDtos = new ArrayList<>();
        GetUserDto getUserDto = GetUserDto.builder()
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .role(Role.ROLE_ADMIN)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        getUserDtos.add(getUserDto);
        when(userService.getAllUsers()).thenReturn(getUserDtos);
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/user/getAllUsers")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        verify(userService).getAllUsers();
        then(result).isEqualTo(objectMapper.writeValueAsString(getUserDtos));
    }

    @Test
    @WithMockUser(username = "11111111111", authorities = "ROLE_ADMIN")
    void deleteUserById() throws Exception {
        long userId = 2;
        GetUserDto getUserDto = GetUserDto.builder()
                .id(userId)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        when(userService.deleteUserById(userId)).thenReturn(getUserDto);

        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/user/deleteUserById?id=2")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        verify(userService).deleteUserById(userId);
        then(result).isEqualTo(objectMapper.writeValueAsString(getUserDto));
    }

    @Test
    void updateUser() throws Exception {
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .UserId(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();
        GetUserDto getUserDto = GetUserDto.builder()
                .id(2)
                .name("yusuf")
                .surname("demir")
                .phoneNumber("1111111111")
                .tcIdentityNumber("11111111111")
                .monthlyIncome(10000)
                .dateOfBirth(LocalDate.of(2022, 2, 2))
                .build();

        when(userService.UpdateUser(updateUserDto)).thenReturn(getUserDto);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/user/updateUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDto))
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        verify(userService).UpdateUser(updateUserDto);
        then(result).isEqualTo(objectMapper.writeValueAsString(getUserDto));
    }
}