package com.example.hmsUser.service;

import com.example.hmsUser.dto.requestDto.UserLoginRequest;
import com.example.hmsUser.dto.responseDto.BaseApiResponse;
import com.example.hmsUser.entity.Users;
import com.example.hmsUser.implementation.UsersImpl;
import com.example.hmsUser.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UsersService implements UsersImpl {

    @Autowired
    public UsersRepository userRepository;
    @Autowired
    public JwtService jwtService;
    @Autowired
    public PasswordEncoder passwordEncoder;

    @Override
    public BaseApiResponse loginUser(UserLoginRequest userLoginRequest) {
        BaseApiResponse response = new BaseApiResponse();

        // Check if user exists by email
        Optional<Users> existingUserOpt = userRepository.findByEmail(userLoginRequest.getEmail());

        if (existingUserOpt.isEmpty()) {
            response.setSuccess(0);
            response.setMessage("User not found");
            return response;
        }

        Users users = existingUserOpt.get();

        // Check if the provided password matches the stored password
        if (passwordEncoder.matches(userLoginRequest.getPassword(), users.getPassword())) {

            // Get roles associated with the Users
            String role = users.getRole();
            System.out.println(role);

            // Generate JWT token with roles as a claim
            String token = jwtService.generateToken(users.getEmail(), role);

            // Respond with success and the JWT token
            response.setSuccess(1);
            response.setMessage("Login successful");
            response.setData(Collections.singletonMap("token", token)); // Send token in response
        } else {
            response.setSuccess(0);
            response.setMessage("Invalid password");
        }

        return response;
    }

}
