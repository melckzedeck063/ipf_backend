package com.zedeck.ipftestscenario.servicesImpl;
import com.zedeck.ipftestscenario.dtos.LoginDto;
import com.zedeck.ipftestscenario.dtos.LoginResponseDto;
import com.zedeck.ipftestscenario.jwt.JWTUtils;
import com.zedeck.ipftestscenario.models.UserAccount;
import com.zedeck.ipftestscenario.repositories.UserAccountRepository;
import com.zedeck.ipftestscenario.services.AuthService;
import com.zedeck.ipftestscenario.utils.GlobalExceptionHandler;
import com.zedeck.ipftestscenario.utils.Response;
import com.zedeck.ipftestscenario.utils.ResponseCode;
import com.zedeck.ipftestscenario.utils.exceptions.GlobalException;
import com.zedeck.ipftestscenario.utils.exceptions.ResourceNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserAccountRepository accountRepository;

    @Autowired
    private JWTUtils jwtUtils;

    private GlobalExceptionHandler globalExceptionHandler;


    @Override
    public Response<LoginResponseDto> login(LoginDto loginDto) {
        try {
            log.info("LOGIN CREDENTIALS : " , loginDto);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = jwtUtils.generateJwtToken(authentication);
            String refreshToken = UUID.randomUUID().toString();

            Optional<UserAccount> accountOptional = accountRepository.findFirstByUsername(authentication.getName());

            if(accountOptional.isEmpty())
                throw  new GlobalException(ResponseCode.FAIL,"User not found");

            return getLoginResponseResponse(accountOptional, jwtToken, refreshToken);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Invalid username or password");
    }

    @NotNull
    private Response<LoginResponseDto> getLoginResponseResponse(Optional<UserAccount> accountOptional, String jwtToken, String refreshToken) {
        if (accountOptional.isPresent()){

            UserAccount account = accountOptional.get();
            account.setRefreshToken(refreshToken);
            account.setRefreshTokenCreatedAt(LocalDateTime.now());
            accountRepository.save(account);
            LoginResponseDto response = new LoginResponseDto(
                    jwtToken,
                    refreshToken,
                    "Bearer",
                    account.getUsername(),
                    account.getUserType(),
                    account.getFirstName(),
                    account.getLastName()
            );

            return new Response<>(false, ResponseCode.SUCCESS, response, null, "Login successful");

        }
        else  {
            throw  new ResourceNotFoundException("No record for the provided account");
        }
    }





}
