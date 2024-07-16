package com.middleware.service.payment_gateway.service;

import com.middleware.service.payment_gateway.dtos.LoginRequest;
import com.middleware.service.payment_gateway.dtos.LoginResponse;
import com.middleware.service.payment_gateway.model.AppUser;
import com.middleware.service.payment_gateway.repository.UserRepository;
import com.middleware.service.payment_gateway.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.middleware.service.payment_gateway.model.AppUser;
import com.middleware.service.payment_gateway.repository.UserRepository;
import com.middleware.service.payment_gateway.security.JwtUtil;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));
        AppUser user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String jwt = jwtUtil.createJwtToken(user);
        return new LoginResponse(user.getUsername(), jwt);
    }
}
