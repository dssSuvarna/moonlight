package com.builderbackend.app.clientPortal.controllers;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.builderbackend.app.dtos.BusinessDTO;
import com.builderbackend.app.clientPortal.services.BusinessInfoService;
import com.google.gson.Gson;

@RestController
@RequestMapping("client-business")
public class BusinessInfoController {

    @Autowired
    BusinessInfoService businessInfoService;

    @GetMapping("/businessInfo")
    public ResponseEntity<BusinessDTO> getBusinessInfo(
            @CookieValue(value = "businessId", required = true) String businessId) {

        BusinessDTO response = businessInfoService.getBusInfo(businessId);

        // this will return 200-OK & the response if response !Null, otherwise not found
        // if resposnse ==null
        return ResponseEntity.ofNullable(response);
    }

    @GetMapping("/getBusiness")
    public String getBusiness(@RequestParam String businessId) {
        BusinessDTO response = businessInfoService.getBusiness(businessId);
        return convertToJson(response);
    }

    private String convertToJson(Object object) {
        return new Gson().toJson(object);
    }
}
