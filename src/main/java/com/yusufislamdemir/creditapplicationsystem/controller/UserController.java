package com.yusufislamdemir.creditapplicationsystem.controller;

import com.yusufislamdemir.creditapplicationsystem.dto.request.CreateUserDto;
import com.yusufislamdemir.creditapplicationsystem.dto.request.UpdateUserDto;
import com.yusufislamdemir.creditapplicationsystem.dto.response.GetUserDto;
import com.yusufislamdemir.creditapplicationsystem.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/createUser")
    public ResponseEntity<GetUserDto> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(createUserDto));
    }


    @GetMapping("/getUserById")
    public ResponseEntity<GetUserDto> getUserById(@RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/giveAdminRoleToUser")
    public ResponseEntity<GetUserDto> giveAdminRoleToUser(@RequestParam long userId){
        return ResponseEntity.status(HttpStatus.OK).body(userService.giveAdminRoleToUser(userId));
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<GetUserDto>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }


    @Secured("ROLE_ADMIN")
    @DeleteMapping("/deleteUserById")
    public ResponseEntity<GetUserDto> deleteUserById(@RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUserById(id));
    }

    @PutMapping("/updateUser")
    public ResponseEntity<GetUserDto> updateUser(@Valid @RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.UpdateUser(updateUserDto));
    }

}
