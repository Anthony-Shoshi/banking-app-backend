package com.groupfour.bankingapp.Controllers;

import com.groupfour.bankingapp.Models.Guitar;
import com.groupfour.bankingapp.Services.GuitarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("guitars")
public class GuitarController {

    @Autowired
    private GuitarService guitarService;

    @GetMapping
    public ResponseEntity getAllGuitars() {
        return ResponseEntity.ok(guitarService.getAllGuitars());
    }

    @PostMapping
    public ResponseEntity addGuitar(@RequestBody Guitar guitar) {
        return ResponseEntity.status(201).body(
                Collections.singletonMap("id", guitarService.addGuitar(guitar))
        );
    }
}