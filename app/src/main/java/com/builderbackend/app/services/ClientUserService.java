package com.builderbackend.app.services;

import lombok.Data;
import java.lang.Exception;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.builderbackend.app.models.User;
import com.builderbackend.app.dtos.UserDTO;
import com.builderbackend.app.dtos.ClientUserDTO;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.mappers.UserMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.apache.commons.lang3.RandomStringUtils;
import com.builderbackend.app.exceptions.UserAlreadyExistsException;
import com.builderbackend.app.models.EmailDetails;
import com.builderbackend.app.services.LogoService;
import com.builderbackend.app.dtos.LogoDTO;

@RequiredArgsConstructor
@Service
@Data
public class ClientUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    EmailService emailService;


    @Autowired
    LogoService logoService;

    @Value("${login.path}")
    private String loginPath;

    public ClientUserDTO createUser(UserDTO userDTO) throws UserAlreadyExistsException {
        // Setting UUID for Update
        String userId = UUID.randomUUID().toString();
        userDTO.setUserId(userId);

        // need to create a temp password
        // this will have to be reset on clients first login
        String tempPassword = RandomStringUtils.randomAlphanumeric(10);

        // encrypt password with bcrypt
        // make sure we this is always the same encryption class as used in
        // WebSecurityConfig
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userDTO.setPassword(encoder.encode(tempPassword));

        userDTO.setRole("client");

        // Convert DTO to Entity
        User userEntity = userMapper.convert_UserDTO_to_User(userDTO);

        // MODIFY THIS TO CHECK EMAIL ONCE WE CHANGE SECURITY CONFIGS TO CHECK EMAIL
        // check if username already exists
        if (userRepository.getReferenceByUserName(userDTO.getUserName()) != null) {
            throw new UserAlreadyExistsException("There is an account with that username: "
                    + userDTO.getUserName());
        }

        // Save to the database
        userRepository.save(userEntity);

        ClientUserDTO clientUserDTO = userMapper.convert_User_to_UserClientDTO(userEntity);

        // email the temp account credientials to client
        // todo: handle case where wrong email is uese or email connot be sent. as of
        // now emailService.sendHtmlMail will catch errors and return an error message
        // string
        // we should indicated the error to the user and ask them to re-enter email
        EmailDetails emailDetails = new EmailDetails();
        String businessName = userRepository.findBusinessNameByUserId(userDTO.getUserId());
        String url = loginPath + "?token="  + userDTO.getBusinessId();


        List<LogoDTO> logos = logoService.getLogosForBusinessId(userDTO.getBusinessId());
        String imageUrl = null;

        for (LogoDTO logo : logos){
            if ("emailLogo".equals(logo.getLogoType())) {
                imageUrl = logo.getFileInfoDTO().getFileUrl();
                break; 
            }
        }

        String imageHtml = "";
        if (imageUrl != null && !imageUrl.isEmpty())
        {
            imageHtml = "<p><img src='" + loginPath + "/api/" + imageUrl + "' alt='' style='width:100%; max-width:200px; height:auto;'/></p>";
        }

        emailDetails.setSubject("Welcome! Sign into your " + businessName + " account powered by MoonlightConnect");
        emailDetails.setMsgBody("<html>"
                + "<body>"
                + "<p>" + "Sign into your " + businessName + " account using the temporary credential." + "</p>"
                + "<p></p>"
                + "<p>" + "User Name: " + userDTO.getUserName() + "</p>"
                + "<p>" + "Password: " + tempPassword + "</p>"
                + "<p>" + "</p>"
                + "<a href=" + url + " target='_blank'>"
                + "<button style='background-color: #4CAF50; color: white; padding: 15px 32px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; margin: 4px 2px; cursor: pointer; border: none; border-radius: 4px;'>login</button>"
                + "</a>"
                + imageHtml 
                + "</body>"
                + "</html>");

        emailDetails.setRecipient(userDTO.getEmail());
        emailService.sendCustomHtmlMail(emailDetails);

        return clientUserDTO;
    }

    public List<ClientUserDTO> getUserFromEmployeeId(String userId) {
        List<User> userList = new ArrayList<User>();
        String businessId;

        try {
            businessId = userRepository.findBusinessIdByUserId(userId);
            userList = userRepository.findByBusinessBusinessIdAndRole(businessId, "client");

        } catch (Exception e) {
            // should probably log this
            System.out.println("Exception: " + e);
            throw e;
        }

        List<ClientUserDTO> ClientUserDTOList = new ArrayList<ClientUserDTO>();

        for (User userEntity : userList) {
            ClientUserDTOList.add(userMapper.convert_User_to_UserClientDTO(userEntity));
        }
        return ClientUserDTOList;

    }

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

    public ClientUserDTO editClientUserInfo (ClientUserDTO clientUserDTO){

        String clientUserId = clientUserDTO.getUserId();

        User clientUser = userRepository.findById(clientUserId).orElse(null);

        clientUser.setFirstName(clientUserDTO.getFirstName());
        clientUser.setLastName(clientUserDTO.getLastName());
        clientUser.setEmail(clientUserDTO.getEmail());
        clientUser.setPhoneNumber(clientUserDTO.getPhoneNumber());

        userRepository.save(clientUser);

        return userMapper.convert_User_to_UserClientDTO(clientUser);

    }

}
