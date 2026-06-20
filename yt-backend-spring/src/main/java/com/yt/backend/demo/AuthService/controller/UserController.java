package com.yt.backend.demo.AuthService.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yt.backend.demo.AuthService.dtos.LoginDto;
import com.yt.backend.demo.AuthService.dtos.RegisterDto;
import com.yt.backend.demo.AuthService.model.UserModel;
import com.yt.backend.demo.AuthService.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserModel register(@Valid @RequestBody RegisterDto registerDto) {
        return userService.register(registerDto);
    }

    @PostMapping("/login")
    public Map<String,String> login(@Valid @RequestBody LoginDto loginDto) {
        Map<String,String> response = new HashMap<>();
        String token = userService.login(loginDto);
        response.put("token", token);
        response.put("status", "200");
        return response;
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);
    
        return ResponseEntity.ok(
                Map.of("message", "User deleted successfully", "status", "true")
        );
    }

    @PostMapping("/validateToken")
    public Map<String, Object> validateToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        boolean isValid = userService.validateToken(token);
        Map<String, Object> response = new HashMap<>();
        response.put("isValid", isValid);
        return response;
    }
}
