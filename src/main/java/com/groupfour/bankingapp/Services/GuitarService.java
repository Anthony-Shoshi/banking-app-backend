package com.groupfour.bankingapp.Services;

import com.groupfour.bankingapp.Models.Guitar;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuitarService {
    private List<Guitar> guitars;

    public GuitarService(List<Guitar> guitars) {
        this.guitars = guitars;
    }

    public List<Guitar> getAllGuitars() {
        return guitars;
    }

    public Integer addGuitar(Guitar guitar) {
        guitars.add(guitar);
        return guitars.indexOf(guitar);
    }
}
