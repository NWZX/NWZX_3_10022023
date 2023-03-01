package com.chatop.estate.payloads.responses;

import com.chatop.estate.model.Rentals;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class RentalResponse {
    private Integer id;
    private String name;
    private String description;
    private Float surface;
    private Float price;
    private List<String> picture;
    private Integer owner_id;
    private String created_at;
    private String updated_at;

    public RentalResponse() {
    }
    public RentalResponse(Rentals rentals) {
        this.id = rentals.getId();
        this.name = rentals.getName();
        this.description = rentals.getDescription();
        this.surface = rentals.getSurface();
        this.price = rentals.getPrice();
        this.picture = Arrays.asList(rentals.getPicture());
        this.owner_id = rentals.getOwnerId();
        this.created_at = new SimpleDateFormat("yyyy/MM/dd").format(rentals.getCreatedAt());
        this.updated_at = rentals.getUpdatedAt() != null ? new SimpleDateFormat("yyyy/MM/dd").format(rentals.getUpdatedAt()) : null;
    }

    public static class RentalResponseArray {
        public List<RentalResponse> rentals;
        public RentalResponseArray(List<RentalResponse> rentals) {
            this.rentals = rentals;
        }

        public List<RentalResponse> getRentals() {
            return rentals;
        }
    }
}
