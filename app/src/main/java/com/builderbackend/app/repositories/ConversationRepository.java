package com.builderbackend.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.builderbackend.app.models.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, String> {
    List<Conversation> findByParticipants_User_UserId(String userId);
    
    List<Conversation> findByProject_ProjectId(String projectId);

    Conversation findByConversationId(String conversationId);
}