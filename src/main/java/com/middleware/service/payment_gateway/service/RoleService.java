package com.middleware.service.payment_gateway.service;

import com.middleware.service.payment_gateway.dtos.Response;
import com.middleware.service.payment_gateway.dtos.RoleRequest;
import com.middleware.service.payment_gateway.enums.ResponseCode;
import com.middleware.service.payment_gateway.exception.DuplicateException;
import com.middleware.service.payment_gateway.exception.NotFoundException;
import com.middleware.service.payment_gateway.model.Role;
import com.middleware.service.payment_gateway.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public Response createRole(RoleRequest roleRequest) {
        Optional<Role> existingRole = roleRepository.findByName(roleRequest.getName());
        if (existingRole.isPresent()) throw new DuplicateException(roleRequest.getName() + " already exists.");
        Role role = modelMapper.map(roleRequest, Role.class);
        roleRepository.save(role);
        return new Response(ResponseCode.SUCCESS);
    }

    public RoleRequest getRole(Long id) {
        Optional<Role> existingRole = roleRepository.findById(id);
        if (existingRole.isEmpty()) throw new NotFoundException("Role not found.");
        return modelMapper.map(existingRole, RoleRequest.class);
    }

    public RoleRequest getRoleByName(String name) {
        Optional<Role> existingRole = roleRepository.findByName(name);
        if (existingRole.isEmpty()) throw new NotFoundException("Role not found.");
        return modelMapper.map(existingRole, RoleRequest.class);
    }

    public List<RoleRequest> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(role -> modelMapper.map(role, RoleRequest.class)).collect(Collectors.toList());
    }
}
