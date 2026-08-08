package com.hospitalmanagement.smartcare.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @GetMapping("/test")
    public String doctorTest() {
        return "DOCTOR access successful";
    }
}
