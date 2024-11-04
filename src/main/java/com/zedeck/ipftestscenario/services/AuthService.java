package com.zedeck.ipftestscenario.services;


import com.zedeck.ipftestscenario.dtos.LoginDto;
import com.zedeck.ipftestscenario.dtos.LoginResponseDto;
import com.zedeck.ipftestscenario.dtos.UserAccountDto;
import com.zedeck.ipftestscenario.models.UserAccount;
import com.zedeck.ipftestscenario.utils.Response;

public interface AuthService {

    Response<LoginResponseDto> login(LoginDto loginDto);

    Response<UserAccount> registerUser(UserAccountDto userAccountDto);

    Response<UserAccount> updateUser(UserAccountDto userAccountDto);

    Response<UserAccount> getProfile();

}

