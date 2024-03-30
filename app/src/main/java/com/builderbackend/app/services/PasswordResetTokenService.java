package com.builderbackend.app.services;

import org.springframework.stereotype.Service;

import com.builderbackend.app.repositories.PasswordResetTokenRepository;
import com.builderbackend.app.models.PasswordResetToken;
import com.builderbackend.app.models.User;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetToken createToken(User user) {
        // check if a stale or old Password reset token already exists
        PasswordResetToken oldToken = passwordResetTokenRepository.findByUser(user);
        if(oldToken != null){
            deleteToken(oldToken);
        }

        // create new token
        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(LocalDateTime.now().plusMinutes(PasswordResetToken.EXPIRATION));
        return passwordResetTokenRepository.save(token);
    }

    public PasswordResetToken getToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    public boolean isTokenExpired(PasswordResetToken token) {
        return token.getExpiryDate().isBefore(LocalDateTime.now());
    }

    public void deleteToken(PasswordResetToken token) {
        passwordResetTokenRepository.delete(token);
    }

    // Other necessary methods
}