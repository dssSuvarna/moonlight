package com.builderbackend.app.clientPortal.services;

import lombok.Data;
import java.lang.Exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.builderbackend.app.models.User;
import com.builderbackend.app.dtos.ClientUserDTO;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.mappers.UserMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Data
public class ClientPortalUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    public ClientUserDTO getUserForUserId(String userId) {
        User user = new User();

        try {
            user = userRepository.getReferenceById(userId);
        }

        catch (Exception e) {
            // should probably log this
            System.out.println("Exception: " + e);
            throw e;
        }
        ClientUserDTO clientUserDTO = new ClientUserDTO();

        clientUserDTO = userMapper.convert_User_to_UserClientDTO(user);
        return clientUserDTO;

    }

}
