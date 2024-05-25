package com.groupfour.bankingapp.Models.DTO;

public record LoginResponseDTO(String email, String token, String role, String firstName, String lastName){
}
