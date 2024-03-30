package com.builderbackend.app.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.builderbackend.app.dtos.UserDTO;
import com.builderbackend.app.dtos.ClientUserDTO;
import com.builderbackend.app.services.ClientUserService;
import com.builderbackend.app.exceptions.UserAlreadyExistsException;
import com.google.gson.Gson;

import org.springframework.http.ResponseEntity;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("client")
public class UserController {

    private final ClientUserService userService;

    @Autowired
    public UserController(ClientUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDTO userDTO) throws UserAlreadyExistsException {
        ClientUserDTO response;
        response = userService.createUser(userDTO);
        return ResponseEntity.ok(convertToJson(response));
    }

    @GetMapping("/get-users")
    public String getUsers(@RequestParam String userId) {
        List<ClientUserDTO> clientUserDTO;
        try {
            clientUserDTO = userService.getUserFromEmployeeId(userId);
        } catch (Exception e) {
            return e.getMessage(); // need better error handling.
        }

        return convertToJson(clientUserDTO);
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


    @PutMapping("/modify")
    public ResponseEntity<ClientUserDTO> editClientUserInfo(@RequestBody ClientUserDTO clientUser){
        ClientUserDTO updatedClientUserInfo = userService.editClientUserInfo(clientUser);
        return ResponseEntity.ofNullable(updatedClientUserInfo);
    }

    private String convertToJson(Object object) {
        return new Gson().toJson(object);
    }

}
