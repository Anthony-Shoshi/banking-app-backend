package com.groupfour.bankingapp.Models.DTO;

//public record LoginResponseDTO(Long userId, Long customerId, String email, String token, String role, String firstName, String lastName){
public record LoginResponseDTO(String email, String token){
}
