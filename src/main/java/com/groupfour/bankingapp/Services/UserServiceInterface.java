package com.groupfour.bankingapp.Services;

import javax.naming.AuthenticationException;
import com.groupfour.bankingapp.Models.DTO.LoginRequestDTO;
import com.groupfour.bankingapp.Models.DTO.LoginResponseDTO;

public interface UserServiceInterface {
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws AuthenticationException;
}
