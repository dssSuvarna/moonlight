package com.builderbackend.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ForgotPasswordDTO {
    
    String token;
    String newPassword;
}
