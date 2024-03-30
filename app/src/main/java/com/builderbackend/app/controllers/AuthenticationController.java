package com.builderbackend.app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationController {
    
    @GetMapping("/api/is-authenticated")
    public ResponseEntity<?> isAuthenticated(HttpServletRequest request) {
        if ( request.isUserInRole("ROLE_employee")) { // or any other role or condition you have
            return ResponseEntity.ok().body("isEmployee");
        } else if (request.isUserInRole("ROLE_employee_admin")) {
	        return ResponseEntity.ok().body("isEmployeeAdmin");
	    } else if (request.isUserInRole("ROLE_client")) {
	        return ResponseEntity.ok().body("isClient");
	    } else {
            return ResponseEntity.ok().body("NotAuthenticated");
        }
}
}
