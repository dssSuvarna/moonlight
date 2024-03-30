package com.builderbackend.app.mappers;

import com.builderbackend.app.dtos.SelectionsDTO;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.repositories.BusinessRepository;
import com.builderbackend.app.repositories.ProjectRepository;
import com.builderbackend.app.models.Selections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SelectionsMapper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    FileInfoMapper fileInfoMapper;

    /*
     * Converts Selections Entity to selectionsDTO
     */
    public SelectionsDTO convert_Selections_to_selectionsDTO(Selections selections) {
        SelectionsDTO selectionsDTO = new SelectionsDTO();

        selectionsDTO.setSelectionId(selections.getSelectionId());
        selectionsDTO.setName(selections.getName());
        selectionsDTO.setDescription(selections.getDescription());
        selectionsDTO.setPartNumber(selections.getPartNumber());
        selectionsDTO.setCost(selections.getCost());
        selectionsDTO.setClientConfirmation(selections.getClientConfirmation());
        selectionsDTO.setUserId(selections.getUser().getUserId());
        selectionsDTO.setBusinessId(selections.getBusiness().getBusinessId());
        selectionsDTO.setProjectId(selections.getProject().getProjectId());

        return selectionsDTO;

    }

    /*
     * Converts SelectionsDTO to Selections Entity
     */
    public Selections convert_selectionsDTO_to_Selections(SelectionsDTO selectionsDTO) {
        Selections selections = new Selections();

        selections.setSelectionId(selectionsDTO.getSelectionId());
        selections.setName(selectionsDTO.getName());
        selections.setDescription(selectionsDTO.getDescription());
        selections.setPartNumber(selectionsDTO.getPartNumber());
        selections.setCost(selectionsDTO.getCost());
        selections.setClientConfirmation(selectionsDTO.getClientConfirmation());
        selections.setUser(userRepository.findById(selectionsDTO.getUserId()).orElse(null));
        selections.setBusiness(businessRepository.findById(selectionsDTO.getBusinessId()).orElse(null));
        selections.setProject(projectRepository.findById(selectionsDTO.getProjectId()).orElse(null));

        return selections;
    }

}
