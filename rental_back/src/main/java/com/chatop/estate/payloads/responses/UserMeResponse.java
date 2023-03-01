package com.chatop.estate.payloads.responses;

import com.chatop.estate.model.Users;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class UserMeResponse {
    public Integer id;

    public String email;
    public String name;

    public String created_at;

    public String updated_at;

    public UserMeResponse(Integer id, String email, String name, Date created_at, Date updated_at) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.created_at = new SimpleDateFormat("yyyy/MM/dd").format(created_at);
        this.updated_at = updated_at != null ? new SimpleDateFormat("yyyy/MM/dd").format(updated_at) : null;
    }
    public UserMeResponse(Users users) {
        this.id = users.getId();
        this.email = users.getEmail();
        this.name = users.getName();
        this.created_at = new SimpleDateFormat("yyyy/MM/dd").format(users.getCreatedAt());
        this.updated_at = users.getUpdatedAt() != null ? new SimpleDateFormat("yyyy/MM/dd").format(users.getUpdatedAt()) : null;
    }

    public UserMeResponse() {
    }
}
