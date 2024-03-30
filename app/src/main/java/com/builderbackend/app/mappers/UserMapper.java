package com.builderbackend.app.mappers;

import com.builderbackend.app.dtos.UserDTO;
import com.builderbackend.app.models.User;
import com.builderbackend.app.dtos.ClientUserDTO;
import com.builderbackend.app.repositories.BusinessRepository;
import com.builderbackend.app.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BusinessRepository businessRepository;

    public UserDTO convert_User_to_UserDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(user.getUserId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUserName(user.getUserName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());
        userDTO.setBusinessId(user.getBusiness().getBusinessId());

        return userDTO;
    }

    public User convert_UserDTO_to_User(UserDTO userDTO) {
        User user = new User();

        user.setUserId(userDTO.getUserId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUserName(userDTO.getUserName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setRole(userDTO.getRole());
        user.setBusiness(businessRepository.findByBusinessId(userDTO.getBusinessId()));
        return user;
    }

    public ClientUserDTO convert_User_to_UserClientDTO(User user) {
        ClientUserDTO clientUserDTO = new ClientUserDTO();

        clientUserDTO.setUserId(user.getUserId());
        clientUserDTO.setFirstName(user.getFirstName());
        clientUserDTO.setLastName(user.getLastName());
        clientUserDTO.setUserName(user.getUserName());
        clientUserDTO.setEmail(user.getEmail());
        clientUserDTO.setPhoneNumber(user.getPhoneNumber());
        clientUserDTO.setRole(user.getRole());
        clientUserDTO.setBusinessId(user.getBusiness().getBusinessId());
        return clientUserDTO;
    }

}
