package com.builderbackend.app.clientPortal.services;

import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.AllArgsConstructor;

import com.builderbackend.app.dtos.BusinessDTO;
import com.builderbackend.app.repositories.BusinessRepository;
import com.builderbackend.app.models.Business;
import com.builderbackend.app.mappers.BusinessMapper;

@AllArgsConstructor
@Service
public class BusinessInfoService {

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    BusinessMapper businessMapper;

    /**
     * 
     * @param businessId
     * @return the busness info in a dto
     */
    public BusinessDTO getBusInfo(String businessId) {
        // get entity from db
        Business businessEntity = businessRepository.findByBusinessId(businessId);

        if (businessEntity == null) {
            System.out.println("businessId: " + businessId + " not found when trying to getBusInfo");
            return null;
        }

        // conver to dto
        BusinessDTO businessDTO = businessMapper.convert_Business_to_BusinessDTO(businessEntity);

        // remove unneccessary info
        businessDTO.setSubscriptionType(null);

        return businessDTO;

    }

    public BusinessDTO getBusiness(String businessId) {

        Optional<Business> business = businessRepository.findById(businessId);

        BusinessDTO businessDTO = new BusinessDTO();
        if (business.isPresent()) {

            businessDTO = businessMapper.convert_Business_to_BusinessDTO(business.get());

        }
        return businessDTO;

    }
}
