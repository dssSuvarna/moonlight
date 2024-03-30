package com.builderbackend.app.controllers;

import java.util.UUID;
import com.builderbackend.app.models.User;
import com.builderbackend.app.models.Business;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.repositories.BusinessRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;


// this call is a class we can use when we are experimenting and need to create a new api on the fly
// eventually need to remove this class

@RestController
public class RandomTestsController {

    private UserRepository userRepository;
    private BusinessRepository businessRepository;

    @Autowired
    public RandomTestsController(UserRepository userRepository, BusinessRepository businessRepository){
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
    }

    @PostMapping("/random/add/User")
    public String addUser(@RequestBody User user){

        System.out.println("post request received");
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        System.out.println("UUID set");



        String response = new Gson().toJson(user);

        Business b = new Business();
        b.setBusinessId("43f7eb7b-9d94-4cdf-8a51-dc448c19cd34");
        b.setBusinessName("OurFirstBusiness");
        b.setEmail("business@email.com");
        b.setPhoneNumber("(226) 555-1234");
        System.out.println(b.getBusinessId());
        user.setBusiness(b);
        System.out.println(user.getBusiness());

        userRepository.save(user);
        System.out.println("Saved!");

        return response;
    }

    @PostMapping("/random/add/business")
    public String addUser(@RequestBody Business business){

        System.out.println("post request received");
        String businessId = UUID.randomUUID().toString();
        business.setBusinessId(businessId);
        System.out.println("UUID set");

        String response = new Gson().toJson(business);

        businessRepository.save(business);
        System.out.println("Saved!");

        return response;
    }
    
}


