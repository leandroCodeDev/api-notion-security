package com.api.notion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("teste")
public class TesteController {

    @GetMapping
    public ResponseEntity teste(){
        return ResponseEntity.ok("teste");
    }

    @PostMapping
    public ResponseEntity teste1(){
        return ResponseEntity.ok("teste post");
    }
}
