package com.chatop.estate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatop.estate.model.Users;

import java.util.Optional;

@Repository
public interface IUsersRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByEmail(String email);

}
