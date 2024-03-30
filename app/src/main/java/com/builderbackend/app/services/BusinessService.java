package com.builderbackend.app.services;

import lombok.Data;

import java.util.UUID;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.builderbackend.app.models.Business;
import com.builderbackend.app.dtos.BusinessDTO;
import com.builderbackend.app.repositories.BusinessRepository;
import com.builderbackend.app.mappers.BusinessMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Data
public class BusinessService {

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    BusinessMapper businessMapper;

    public BusinessDTO createBusiness(BusinessDTO businessDTO) {

        // Setting UUID for Update
        String businessId = UUID.randomUUID().toString();
        businessDTO.setBusinessId(businessId);

        // Convert DTO to Entity
        Business businessEntity = businessMapper.convert_BusinessDTO_to_Business(businessDTO);

        // Save to the database
        businessRepository.save(businessEntity);

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
