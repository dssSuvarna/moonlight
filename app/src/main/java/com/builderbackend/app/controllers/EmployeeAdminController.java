package com.builderbackend.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.builderbackend.app.dtos.ClientUserDTO;
import com.builderbackend.app.dtos.UserDTO;
import com.builderbackend.app.exceptions.UserAlreadyExistsException;
import com.builderbackend.app.services.EmployeeAdminService;

@RestController
@RequestMapping("/Employee")
public class EmployeeAdminController {

    @Autowired
    EmployeeAdminService employeeAdminService;

    public EmployeeAdminController(){

    }
    
    // used to create a new employee
    // Note: even thought its called clientUserDTO, its generic and can hold either clients or employees
    @PostMapping("/Create")
    ResponseEntity<?> createNewEmployee(@RequestBody UserDTO newEmployeeDTO) throws UserAlreadyExistsException {

        ClientUserDTO newEmployee = employeeAdminService.createNewEmployee(newEmployeeDTO);
        return ResponseEntity.ofNullable(newEmployee);
    }

    //user to get all employees for a business
    @GetMapping("/Get-All")
    ResponseEntity<?> getAllEmployees(@RequestParam("businessId") String businessId){
        List<ClientUserDTO> employeeList = employeeAdminService.getAllEmployees(businessId);
        return ResponseEntity.ofNullable(employeeList);
    }
}
