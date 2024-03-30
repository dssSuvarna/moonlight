package com.builderbackend.app.clientPortal.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.builderbackend.app.dtos.ClientUserDTO;
import com.builderbackend.app.clientPortal.services.ClientPortalUserService;
import com.google.gson.Gson;

@RestController
@RequestMapping("clientPortal-user")
public class ClientUserController {

    private final ClientPortalUserService userService;

    @Autowired
    public ClientUserController(ClientPortalUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get-single-user")
    public String getSingleUser(@RequestParam String userId) {
        ClientUserDTO clientUserDTO;
        try {
            clientUserDTO = userService.getUserForUserId(userId);
        } catch (Exception e) {
            return e.getMessage(); // need better error handling.
        }

        return convertToJson(clientUserDTO);
    }

    private String convertToJson(Object object) {
        return new Gson().toJson(object);
    }
}
