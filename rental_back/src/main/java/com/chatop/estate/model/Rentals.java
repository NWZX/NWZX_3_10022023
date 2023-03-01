package com.chatop.estate.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "rentals")
public class Rentals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "surface")
    private Float surface;

    @Column(name = "price")
    private Float price;

    @Column(name = "picture")
    private String picture;

    @Column(name= "owner_id")
    private Integer ownerId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    @Nullable
    private Date updatedAt;

    public Rentals() {
        this.createdAt = new Date();
    }

    public Rentals(String name, String description, Float surface, Float price, String picture, Integer ownerId) {
        this.name = name;
        this.description = description;
        this.surface = surface;
        this.price = price;
        this.picture = picture;
        this.ownerId = ownerId;
        this.createdAt = new Date();
    }

    public String getPictureFileName() {
        return this.picture.substring(this.picture.lastIndexOf("/") + 1);
    }
}
