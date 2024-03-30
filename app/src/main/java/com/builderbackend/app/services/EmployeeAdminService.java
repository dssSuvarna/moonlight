package com.builderbackend.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.builderbackend.app.dtos.ClientUserDTO;
import com.builderbackend.app.dtos.UserDTO;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.exceptions.UserAlreadyExistsException;
import com.builderbackend.app.mappers.UserMapper;
import com.builderbackend.app.models.EmailDetails;
import com.builderbackend.app.models.User;
import com.builderbackend.app.services.LogoService;
import com.builderbackend.app.dtos.LogoDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;

@Service
public class EmployeeAdminService {

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

    public ClientUserDTO createNewEmployee(UserDTO newEmployeeDTO) throws UserAlreadyExistsException {

        newEmployeeDTO.setUserId(UUID.randomUUID().toString());

        // need to create a temp password
        // this will have to be reset on clients first login
        String tempPassword = RandomStringUtils.randomAlphanumeric(10);

        // encrypt password with bcrypt
        // make sure we this is always the same encryption class as used in
        // WebSecurityConfig
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        newEmployeeDTO.setPassword(encoder.encode(tempPassword));

        newEmployeeDTO.setRole("employee");

        // Convert DTO to Entity
        User newEmployee = userMapper.convert_UserDTO_to_User(newEmployeeDTO);

        // MODIFY THIS TO CHECK EMAIL ONCE WE CHANGE SECURITY CONFIGS TO CHECK EMAIL
        // check if username already exists
        if (userRepository.getReferenceByUserName(newEmployeeDTO.getUserName()) != null) {
            throw new UserAlreadyExistsException("There is an account with that username: "
                    + newEmployeeDTO.getUserName());
        }

        // Save to the database
        userRepository.save(newEmployee);

        // convert to a ClientUserDTO which doesnt have password field
        ClientUserDTO employeeDTO = userMapper.convert_User_to_UserClientDTO(newEmployee);

        // email the temp account credientials to client
        // todo: handle case where wrong email is uese or email connot be sent. as of
        // now emailService.sendHtmlMail will catch errors and return an error message
        // string
        // we should indicated the error to the user and ask them to re-enter email
        EmailDetails emailDetails = new EmailDetails();
        String businessName = userRepository.findBusinessNameByUserId(employeeDTO.getUserId());
        String url =  loginPath + "?token=" + employeeDTO.getBusinessId();


        List<LogoDTO> logos = logoService.getLogosForBusinessId(employeeDTO.getBusinessId());
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
                + "<p>" + "User Name: " + employeeDTO.getUserName() + "</p>"
                + "<p>" + "Password: " + tempPassword + "</p>"
                + "<a href=" + url + " target='_blank'>"
                + "<button style='background-color: #4CAF50; color: white; padding: 15px 32px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; margin: 4px 2px; cursor: pointer; border: none; border-radius: 4px;'>login</button>"
                + "</a>"
                + imageHtml 
                + "</body>"
                + "</html>");

        emailDetails.setRecipient(employeeDTO.getEmail());
        emailService.sendCustomHtmlMail(emailDetails);

        return employeeDTO;
    }

    // return a list of all employees for a given business
    public List<ClientUserDTO> getAllEmployees(String businessId) {
        List<User> userList = new ArrayList<User>();

        try {
            userList = userRepository.findByBusinessBusinessIdAndRole(businessId, "employee");

        } catch (Exception e) {
            // should probably log this
            System.out.println("Exception: " + e);
            throw e;
        }

        if (userList.size() == 0) {
            // no employees for the business
            return null;
        }

        List<ClientUserDTO> ClientUserDTOList = new ArrayList<ClientUserDTO>();

        for (User userEntity : userList) {
            ClientUserDTOList.add(userMapper.convert_User_to_UserClientDTO(userEntity));
        }
        return ClientUserDTOList;
    }
}
