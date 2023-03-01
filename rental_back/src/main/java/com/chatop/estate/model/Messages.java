package com.chatop.estate.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "messages")
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "message")
    private String message;

    @Column(name = "rental_id")
    private Integer rentalId;

    @Column(name = "user_id")
    private Integer userId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    public Messages() {
        this.createdAt = new Date();
    }
    public Messages(String message, Integer rentalId, Integer userId) {
        this.message = message;
        this.rentalId = rentalId;
        this.userId = userId;
        this.createdAt = new Date();
    }
}
