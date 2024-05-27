package com.groupfour.bankingapp.Models.DTO;

public record RegisterRequestDTO(
        String email,
        String password,
        String firstName,
        String lastName,
        String bsn,
        String phoneNumber,
        String gender,
        String DateOFbirth
) {}
