package com.builderbackend.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.builderbackend.app.models.PasswordResetToken;
import com.builderbackend.app.models.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUser(User user);
}