package com.builderbackend.app.mappers;

import com.builderbackend.app.dtos.BusinessDTO;
import com.builderbackend.app.models.Business;
import com.builderbackend.app.repositories.BusinessRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessMapper {

    @Autowired
    BusinessRepository businessRepository;

    public BusinessDTO convert_Business_to_BusinessDTO(Business business) {
        BusinessDTO businessDTO = new BusinessDTO();

        businessDTO.setBusinessId(business.getBusinessId());
        businessDTO.setBusinessName(business.getBusinessName());
        businessDTO.setEmail(business.getEmail());
        businessDTO.setPhoneNumber(business.getPhoneNumber());
        businessDTO.setSubscriptionType(business.getSubscriptionType());

        return businessDTO;
    }

    public Business convert_BusinessDTO_to_Business(BusinessDTO businessDTO) {
        Business business = new Business();

        business.setBusinessId(businessDTO.getBusinessId());
        business.setBusinessName(businessDTO.getBusinessName());
        business.setEmail(businessDTO.getEmail());
        business.setPhoneNumber(businessDTO.getPhoneNumber());
        business.setSubscriptionType(businessDTO.getSubscriptionType());

        return business;
    }

}
