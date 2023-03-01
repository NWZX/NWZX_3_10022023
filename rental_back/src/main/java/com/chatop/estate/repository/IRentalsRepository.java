package com.chatop.estate.repository;

import com.chatop.estate.model.Rentals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRentalsRepository extends JpaRepository<Rentals, Integer> {
}
