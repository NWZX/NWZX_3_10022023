package com.chatop.estate.payloads.requests;

import com.chatop.estate.model.Messages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class MessageRequest {
    @NotBlank
    public String message;

    @NotEmpty
    public Integer user_id;

    @NotEmpty
    public Integer rental_id;

    public Messages toMessages() {
        return new Messages(message, rental_id, user_id);
    }
}
