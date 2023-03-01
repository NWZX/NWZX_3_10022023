package com.chatop.estate.payloads.requests;

import com.chatop.estate.model.Rentals;
import org.springframework.web.multipart.MultipartFile;


public class RentalRequest {
    public String name;
    public String description;
    public Float surface;
    public Float price;
    public MultipartFile picture;

    public Rentals toRentals(String picturePathName, Integer ownerId) {
        return new Rentals(name, description, surface, price, picturePathName, ownerId);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getSurface() {
        return surface;
    }

    public void setSurface(Float surface) {
        this.surface = surface;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }

}
