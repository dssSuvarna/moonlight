package com.builderbackend.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.builderbackend.app.dtos.ForgotPasswordDTO;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.models.EmailDetails;
import com.builderbackend.app.models.PasswordResetToken;
import com.builderbackend.app.models.User;
import com.builderbackend.app.services.EmailService;
import com.builderbackend.app.services.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Value;


@RestController
@RequestMapping("/ForgotPassword")
public class ForgotPasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private EmailService emailService;

    @Value("${login.path}")
    private String loginPath;

    // endpoint will be used to request a reset password link to email for forgotten
    // passwords
    @PostMapping("/request")
    public String requestPasswordReset(@RequestParam String userName) {
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            // user not found, however we want to keep message ambiguous for security
            // purpose
            return "Password reset link will be sent if userName exists";
        }

        PasswordResetToken token = passwordResetTokenService.createToken(user);
        EmailDetails emailDetails = new EmailDetails();
        String businessName = userRepository.findBusinessNameByUserId(user.getUserId());
        emailDetails.setSubject("Password Reset Link for " + businessName + " Account");
        emailDetails.setRecipient(user.getEmail());

        String url = loginPath + "?token=" + token.getToken();
        emailDetails.setMsgBody("<html>"
                + "<body>"
                + "<p>" + "Please follow link below to reset your " + businessName
                + " account password. This password link will expire in 24 hours. If you did not request a password reset, you can safely ignore this email."
                + "</p>"
                + "<p></p>"
                + "<a href=" + url + " target='_blank'>"
                + "<button style='background-color: #4CAF50; color: white; padding: 15px 32px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; margin: 4px 2px; cursor: pointer; border: none; border-radius: 4px;'>Change Password</button>"
                + "</a>"
                + "</body>"
                + "</html>");

        emailService.sendCustomHtmlMail(emailDetails);

        return "Password reset link will be sent if userName exists";
    }

    // endpoint will let you reset forgotten password
    @PostMapping("/reset")
    public String resetPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        PasswordResetToken resetToken = passwordResetTokenService.getToken(forgotPasswordDTO.getToken());
        if (resetToken == null || passwordResetTokenService.isTokenExpired(resetToken)) {
            return "Invalid or expired password reset token. Please submit a new forgot password request.";
        }

        User user = resetToken.getUser();

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        user.setPassword(bCryptPasswordEncoder.encode(forgotPasswordDTO.getNewPassword()));
        userRepository.save(user);
        passwordResetTokenService.deleteToken(resetToken);

        return "Password reset successfully";
    }

}
