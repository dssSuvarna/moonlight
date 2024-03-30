package com.builderbackend.app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.builderbackend.app.models.Message;


@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    Page<Message> findByConversation_ConversationId(String conversationId, Pageable pageable);
}