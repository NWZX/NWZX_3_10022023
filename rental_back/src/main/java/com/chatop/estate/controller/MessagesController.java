package com.chatop.estate.controller;

import com.chatop.estate.model.Messages;
import com.chatop.estate.model.Rentals;
import com.chatop.estate.model.Users;
import com.chatop.estate.payloads.requests.MessageRequest;
import com.chatop.estate.repository.IMessagesRepository;
import com.chatop.estate.repository.IRentalsRepository;
import com.chatop.estate.repository.IUsersRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages", description = "Messages API")
public class MessagesController {
    private IMessagesRepository messagesRepository;

    private IUsersRepository usersRepository;

    private IRentalsRepository rentalsRepository;
    @Autowired
    public MessagesController(IMessagesRepository messagesRepository, IUsersRepository usersRepository, IRentalsRepository rentalsRepository) {
        this.messagesRepository = messagesRepository;
        this.usersRepository = usersRepository;
        this.rentalsRepository = rentalsRepository;
    }

    /**
     * Create a message for a rental
     * @param messageRequest
     * @param authentication
     * @return
     */
    @ResponseBody
    @PostMapping("")
    @Operation(summary = "Create message", description = "Create a message for a rental")
    @ApiResponse(responseCode = "200", description = "Message sent", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "400", description = "Bad request", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    public ResponseEntity<?> createMessage(@RequestBody MessageRequest messageRequest, Authentication authentication) {
        try {
            // Get current user with email stored in authentication token
            if (authentication instanceof AnonymousAuthenticationToken) {
                throw new Exception("You must be logged in to send a message");
            }
            String currentUserName = authentication.getName();
            Users user = usersRepository.findByEmail(currentUserName).orElseThrow(() -> new Exception("User not found"));
            Rentals rental = rentalsRepository.findById(messageRequest.rental_id).orElseThrow(() -> new Exception("Rental not found"));
            messagesRepository.save(new Messages(messageRequest.message, rental.getId(), user.getId()));

            JSONObject response = new JSONObject();
            response.put("message", "Message sent");
            return ResponseEntity.status(HttpStatus.OK).body(response.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
