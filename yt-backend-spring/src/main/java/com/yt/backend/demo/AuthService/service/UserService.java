package com.yt.backend.demo.AuthService.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yt.backend.demo.AuthService.dtos.LoginDto;
import com.yt.backend.demo.AuthService.dtos.RegisterDto;
import com.yt.backend.demo.AuthService.model.UserModel;
import com.yt.backend.demo.AuthService.repository.UserRepository;
import com.yt.backend.demo.AuthService.utils.JwtUtils;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private KafkaTemplate<String, Long> kafkaTemplate;

    public UserModel register(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        Random random = new Random();

        UserModel user = new UserModel();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setHandle("@" + registerDto.getName().toLowerCase() + random.nextInt(1000));
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setBanner(null);
        user.setImage(null);
        user.setDescription(null);

        return userRepository.save(user);
    }

    public String login(LoginDto loginDto) {
        
        UserModel user = userRepository.findByEmail(loginDto.getEmail());
        if (user == null || !passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        String token = jwtUtils.generateToken(user);
        return token;
    }

    public UserDetails getUserByEmail(String email) {
        UserModel user = userRepository.findByEmail(email);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of() // no roles yet
        );
        return userDetails;
    }

    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.deleteById(userId);

        kafkaTemplate.send("user-deleted-topic", userId);
    }
}
