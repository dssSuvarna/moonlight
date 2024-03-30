package com.builderbackend.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.builderbackend.app.models.Participant;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, String> {
    List<Participant> findByConversation_ConversationId(String conversationId);
}