package com.builderbackend.app.services;

import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import com.builderbackend.app.models.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.builderbackend.app.models.Business;
import com.builderbackend.app.repositories.BusinessRepository;
import org.springframework.beans.factory.annotation.Value;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final String sender;

    @Autowired
    private BusinessRepository businessRepository;

    // Constructor-based dependency injection is recommended for required
    // dependencies
    @Autowired
    public EmailService(JavaMailSender javaMailSender, @Value("${spring.mail.username}") String sender) {
        this.javaMailSender = javaMailSender;
        this.sender = sender;
    }


    @Value("${login.path}")
    String loginPath;

    public String sendSimpleMail(EmailDetails details) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage(); // Instantiate the mail message

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setSubject(details.getSubject());
            mailMessage.setText(details.getMsgBody());

            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        } catch (Exception e) {
            e.printStackTrace(); // It's a good practice to print the stack trace for debugging.
            return "Error while Sending Mail";
        }
    }

    public String sendHtmlMail(EmailDetails details, String url, String imageUrl) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            System.out.println("details ");
            System.out.println(details);

            helper.setFrom(sender);
            helper.setTo(details.getRecipient());
            helper.setSubject(details.getSubject());

            String htmlContent = formatHtmlContent(details.getMsgBody(), url, imageUrl);
            helper.setText(htmlContent, true); // Set to 'true' to send HTML

            javaMailSender.send(message);
            return "Mail Sent Successfully...";
        } catch (MessagingException e) {
            return "Error while Sending Mail";
        }
    }

    public String sendCustomHtmlMail(EmailDetails details) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(sender);
            helper.setTo(details.getRecipient());
            helper.setSubject(details.getSubject());

            String htmlContent = details.getMsgBody();
            helper.setText(htmlContent, true); // Set to 'true' to send HTML

            javaMailSender.send(message);
            return "Mail Sent Successfully...";
        } catch (MessagingException e) {
            return "Error while Sending Mail";
        }
    }

    private String formatHtmlContent(String messageBody, String url, String imageUrl) {
        // Format your HTML content here
   
           // Initialize the image HTML tag only if imageUrl is not null and not empty
        String imageHtml = "";
        if (imageUrl != null && !imageUrl.isEmpty())
        {
            imageHtml = "<p><img src='" + loginPath + "/api/" + imageUrl + "' alt='' style='width:100%; max-width:200px; height:auto;'/></p>";
        }

        return "<html>"
                + "<body>"
                + "<p>" + messageBody + "</p>"
                + "<p>" + "</p>"
                + "<a href=" + url + " target='_blank'>"
                + "<button style='background-color: #4CAF50; color: white; padding: 15px 32px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; margin: 4px 2px; cursor: pointer; border: none; border-radius: 4px;'>Check Notifications</button>"
                + "</a>"
                + imageHtml 
                + "</body>"
                + "</html>";
    }
}
