package com.example.jwt.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public String test(){
        return "Tets Api Success";
    }

}
