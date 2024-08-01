package com.middleware.service.payment_gateway.controller;

import com.middleware.service.payment_gateway.dtos.Response;
import com.middleware.service.payment_gateway.dtos.RoleRequest;
import com.middleware.service.payment_gateway.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Role Management", description = "APIs for managing role accounts")
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "Create Role", description = "Creates a new role account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role successfully created",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Role already exists")
    })
    @PostMapping("/role")
    public ResponseEntity<Response> createRole(@RequestBody @Validated RoleRequest roleRequest) {
        Response response = roleService.createRole(roleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get Role By Id", description = "Retrieves a role account by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role found",
                    content = @Content(schema = @Schema(implementation = RoleRequest.class))),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @GetMapping("/role/{id}")
    public ResponseEntity<RoleRequest> getRole(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.getRole(id));
    }

    @Operation(summary = "Get Role By Name", description = "Retrieves a role account by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role found",
                    content = @Content(schema = @Schema(implementation = RoleRequest.class))),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @GetMapping("/role/{name}")
    public ResponseEntity<RoleRequest> getRole(@PathVariable String name) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.getRoleByName(name));
    }

    @Operation(summary = "Get All Roles", description = "Retrieves all role accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles retrieved",
                    content = @Content(schema = @Schema(implementation = RoleRequest.class)))
    })
    @GetMapping()
    public ResponseEntity<List<RoleRequest>> getAllRoles() {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.getAllRoles());
    }

}
