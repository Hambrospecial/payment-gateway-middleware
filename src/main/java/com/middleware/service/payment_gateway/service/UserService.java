package com.middleware.service.payment_gateway.service;

import com.middleware.service.payment_gateway.dtos.*;
import com.middleware.service.payment_gateway.enums.ResponseCode;
import com.middleware.service.payment_gateway.exception.NotFoundException;
import com.middleware.service.payment_gateway.model.Role;
import com.middleware.service.payment_gateway.model.User;
import com.middleware.service.payment_gateway.repository.RoleRepository;
import com.middleware.service.payment_gateway.repository.UserRepository;
import com.middleware.service.payment_gateway.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));
        String jwt = jwtUtil.createJwtToken(user);
        return new LoginResponse(user.getUsername(), jwt);
    }

    public Response createUser(UserRequest userRequest) {
        Set<Role> roles = userRequest.getRoles().stream().map(name -> roleRepository.findByName(name).orElseThrow(
                () -> new NotFoundException("Role not found: " + name))).collect(Collectors.toSet());
        User user = modelMapper.map(userRequest, User.class);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new Response(ResponseCode.SUCCESS);
    }

    public Response addRoleToUser(String username, List<String> userRoles) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User not found: " + username));
        Set<Role> roles = userRoles.stream().map(role -> roleRepository.findByName(role).orElseThrow(
                () -> new NotFoundException("Role not found: " + role))).collect(Collectors.toSet());
        user.getRoles().addAll(roles);
        userRepository.save(user);
        return new Response(ResponseCode.SUCCESS);
    }

    public UserResponse getUserByName(String username) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isEmpty()) throw new NotFoundException("User not found.");
        return modelMapper.map(existingUser, UserResponse.class);
    }

    public UserResponse getUserById(Long id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) throw new NotFoundException("User not found.");
        return modelMapper.map(existingUser, UserResponse.class);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> modelMapper.map(user, UserResponse.class)).collect(Collectors.toList());
    }
}
