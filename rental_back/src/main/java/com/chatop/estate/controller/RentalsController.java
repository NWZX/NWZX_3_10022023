package com.chatop.estate.controller;

import com.chatop.estate.model.Rentals;
import com.chatop.estate.model.Users;
import com.chatop.estate.payloads.requests.RentalRequest;
import com.chatop.estate.payloads.responses.RentalResponse;
import com.chatop.estate.repository.IRentalsRepository;
import com.chatop.estate.repository.IUsersRepository;
import com.chatop.estate.services.FileUploadServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Rentals", description = "Rentals API")
public class RentalsController {
    private IRentalsRepository rentalsRepository;

    private IUsersRepository usersRepository;
    @Autowired
    public RentalsController(IRentalsRepository rentalsRepository, IUsersRepository usersRepository) {
        this.rentalsRepository = rentalsRepository;
        this.usersRepository = usersRepository;
    }

    /**
     * Get all rentals
     * @return
     */
    @ResponseBody
    @GetMapping("")
    @Operation(summary = "Get rentals", description = "Get all rentals")
    @ApiResponse(responseCode = "200", description = "Rentals found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = RentalResponse.RentalResponseArray.class)))
    public ResponseEntity<?> getRentals() {
        List<Rentals> rentals = rentalsRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(new RentalResponse.RentalResponseArray(rentals.stream().map((v) -> new RentalResponse(v)).toList()));
    }

    /**
     * Get rental by id
     * @param id
     * @return
     */
    @ResponseBody
    @GetMapping("/{id}")
    @Operation(summary = "Get rental", description = "Get a rental by id")
    @ApiResponse(responseCode = "200", description = "Rental found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = RentalResponse.class)))
    @ApiResponse(responseCode = "404", description = "Rental not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    public ResponseEntity<?> getRental(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new RentalResponse(rentalsRepository.findById(id).orElseThrow(() -> new Exception("Rental not found"))));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Create a rental
     * @param authentication
     * @param rental
     * @return
     */
    @ResponseBody
    @PostMapping("")
    @Operation(summary = "Create rental", description = "Create a rental")
    @ApiResponse(responseCode = "200", description = "Rental created", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = RentalResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad request", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    public ResponseEntity<?> createRental(Authentication authentication, RentalRequest rental) {
        try {
            // Get current user with email stored in authentication token
            if (authentication instanceof AnonymousAuthenticationToken) {
                throw new Exception("You must be logged in to create a rental");
            }
            String currentUserName = authentication.getName();
            Users user = usersRepository.findByEmail(currentUserName).orElseThrow(() -> new Exception("User not found"));


            // Define file name and upload directory
            String fileName = UUID.randomUUID().toString() +  '.' + StringUtils.getFilenameExtension(rental.picture.getOriginalFilename());
            String uploadDir = "files/rentals";

            // Save file and create rental
            FileUploadServices.saveFile(uploadDir, fileName, rental.picture);
            rentalsRepository.save(rental.toRentals("http://localhost:3001/" + uploadDir + "/" + fileName, user.getId()));

            JSONObject response = new JSONObject();
            response.put("message", "Rental created !");
            return ResponseEntity.status(HttpStatus.OK).body(response.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Update a rental
     * @param id
     * @param rental
     * @return
     */
    @ResponseBody
    @PutMapping("/{id}")
    @Operation(summary = "Update rental", description = "Update a rental")
    @ApiResponse(responseCode = "200", description = "Rental updated", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = RentalResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad request", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    public ResponseEntity<?> updateRental(@PathVariable Integer id, RentalRequest rental) {
        try {
            // Get old rental
            Rentals oldRental = rentalsRepository.findById(id).orElseThrow(() -> new Exception("Rental not found"));

            // Create new rental
            Rentals newRental = new Rentals();
            newRental.setId(id);
            newRental.setPrice(rental.getPrice() != null ? rental.getPrice() : oldRental.getPrice());
            newRental.setSurface(rental.getSurface() != null ? rental.getSurface() : oldRental.getSurface());
            newRental.setName(rental.getName() != null ? rental.getName() : oldRental.getName());
            newRental.setDescription(rental.getDescription() != null ? rental.getDescription() : oldRental.getDescription());
            newRental.setPicture(oldRental.getPicture());
            newRental.setOwnerId(oldRental.getOwnerId());
            newRental.setCreatedAt(oldRental.getCreatedAt());
            newRental.setUpdatedAt(new Date());

            // Check if picture is empty and if not, save it
            if(rental.getPicture() != null) {
                String fileName = UUID.randomUUID().toString() +  '.' + StringUtils.getFilenameExtension(rental.picture.getOriginalFilename());
                String uploadDir = "files/rentals";

                FileUploadServices.saveFile(uploadDir, fileName, rental.picture);
                newRental.setPicture("http://localhost:3001/" + uploadDir + "/" + fileName);

                // Remove old picture if exists
                if(oldRental.getPicture() != null) {
                    FileUploadServices.removeFile(uploadDir, oldRental.getPictureFileName());
                }
            }

            // Save new rental
            rentalsRepository.save(newRental);

            JSONObject response = new JSONObject();
            response.put("message", "Rental updated !");
            return ResponseEntity.status(HttpStatus.OK).body(response.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
