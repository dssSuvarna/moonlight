package com.builderbackend.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.builderbackend.app.dtos.PasswordChangeRequestDTO;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.models.User;

@RestController
@RequestMapping("password")
public class PasswordResetController {

    @Autowired
    private UserRepository userRepository;

    // this endpoint will allow already authenticated user to change their passwords.
    @PostMapping("/reset")
    public ResponseEntity<?> changeUserPassword(Authentication authentication, @RequestBody PasswordChangeRequestDTO passwordChangeRequest) {
        User user = userRepository.findByUserName(authentication.getName());
        if(user == null){
            // means current user not authenticated 
            // this shouldnt be possible tbh.. this page and endpoint should only be acessable to authenticated users
            // but adding this just incase
            return ResponseEntity.badRequest().body("Unable to change your password at this time. Please try again later.");
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if (!bCryptPasswordEncoder.matches(passwordChangeRequest.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Current password is incorrect.");
        }

        user.setPassword(bCryptPasswordEncoder.encode(passwordChangeRequest.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok().body("Password changed successfully.");
    }


    
}
