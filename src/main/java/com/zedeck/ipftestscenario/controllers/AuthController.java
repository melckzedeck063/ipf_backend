package com.zedeck.ipftestscenario.controllers;

import com.zedeck.ipftestscenario.dtos.LoginDto;
import com.zedeck.ipftestscenario.dtos.LoginResponseDto;
import com.zedeck.ipftestscenario.dtos.UserAccountDto;
import com.zedeck.ipftestscenario.models.UserAccount;
import com.zedeck.ipftestscenario.services.AuthService;
import com.zedeck.ipftestscenario.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        Response<LoginResponseDto> response = authService.login(loginDto);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody UserAccountDto userAccountDto){
        Response<UserAccount> response = authService.registerUser(userAccountDto);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(path = "/update-user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@RequestBody UserAccountDto userAccountDto){
        Response<UserAccount> response = authService.updateUser(userAccountDto);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(){
        Response<UserAccount> response = authService.getProfile();
        return ResponseEntity.ok().body(response);
    }

}

