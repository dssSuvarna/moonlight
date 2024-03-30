package com.builderbackend.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.builderbackend.app.models.InternalNotesAttachment;

@Repository
public interface InternalNotesAttachmentRepository extends JpaRepository<InternalNotesAttachment, String> {
}