package com.groupfour.bankingapp.Models.DTO;

import com.groupfour.bankingapp.Models.CustomerStatus;
import com.groupfour.bankingapp.Models.Gender;
import com.groupfour.bankingapp.Models.User;

public record CustomerGetWithOutAccountDTO(Long userId,
                                           String name,
                                           CustomerStatus status,
                                           String dob,
                                           Gender sex) {
}
