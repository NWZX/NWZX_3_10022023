package com.chatop.estate.repository;

import com.chatop.estate.model.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMessagesRepository extends JpaRepository<Messages, Integer> {
}
