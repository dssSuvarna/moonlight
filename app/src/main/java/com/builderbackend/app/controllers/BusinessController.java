package com.builderbackend.app.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.builderbackend.app.dtos.BusinessDTO;
import com.builderbackend.app.services.BusinessService;
import com.google.gson.Gson;

@RestController
@RequestMapping("business")
public class BusinessController {
    private final BusinessService businessService;

    @Autowired
    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping("/create")
    public String createBusiness(@RequestBody BusinessDTO businessDTO) {
        BusinessDTO response = businessService.createBusiness(businessDTO);
        return convertToJson(response);
    }

    @GetMapping("/getBusiness")
    public String getBusiness(@RequestParam String businessId) {
        BusinessDTO response = businessService.getBusiness(businessId);
        return convertToJson(response);
    }

    private String convertToJson(Object object) {
        return new Gson().toJson(object);
    }

}
