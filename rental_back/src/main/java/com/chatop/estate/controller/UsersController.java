package com.chatop.estate.controller;

import com.chatop.estate.payloads.responses.UserMeResponse;
import com.chatop.estate.repository.IUsersRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "User API")
public class UsersController {

    private IUsersRepository usersRepository;
    @Autowired
    public UsersController(IUsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * Get user by id
     * @param id
     * @return
     */
    @ResponseBody
    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", description = "Get user by id")
    @ApiResponse(responseCode = "200", description = "User found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserMeResponse.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<?> getUserById(@PathVariable("id") Integer id) {
        return usersRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(new UserMeResponse(record)))
                .orElse(ResponseEntity.notFound().build());
    }
}