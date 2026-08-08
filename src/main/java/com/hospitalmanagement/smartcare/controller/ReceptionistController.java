package com.hospitalmanagement.smartcare.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/receptionist")
public class ReceptionistController {

    @GetMapping("/test")
    public String receptionistTest() {
        return "RECEPTIONIST access successful";
    }
}