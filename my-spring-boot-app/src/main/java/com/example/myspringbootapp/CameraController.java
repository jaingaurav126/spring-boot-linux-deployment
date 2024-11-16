package com.example.myspringbootapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CameraController {

    @GetMapping("/")
    public String showCameraPage() {
        return "camera";  // This will return the camera.html page
    }
}
