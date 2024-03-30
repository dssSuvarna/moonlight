package com.builderbackend.app.mappers;

import com.builderbackend.app.dtos.CalanderEventDTO;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.repositories.BusinessRepository;
import com.builderbackend.app.repositories.ProjectRepository;
import com.builderbackend.app.models.CalanderEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalanderMapper {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    ProjectRepository projectRepository;

    public CalanderEventDTO convert_CalanderEvent_to_CalanderEventDTO(CalanderEvent calanderEvent) {
        CalanderEventDTO calanderEventDTO = new CalanderEventDTO();

        calanderEventDTO.setEventId(calanderEvent.getEventId());
        calanderEventDTO.setTitle(calanderEvent.getTitle());
        calanderEventDTO.setStart(calanderEvent.getStart());
        calanderEventDTO.setEnd(calanderEvent.getEnd());
        calanderEventDTO.setAllDay(calanderEvent.getAllDay());

        calanderEventDTO.setEmployeeId(calanderEvent.getEmployee().getUserId());
        calanderEventDTO.setBusinessId(calanderEvent.getBusiness().getBusinessId());
        calanderEventDTO.setProjectId(calanderEvent.getProject().getProjectId());

        return calanderEventDTO;
    }

    public CalanderEvent convert_CalanderEventDTO_to_CalanderEvent(CalanderEventDTO calanderEventDTO) {
        CalanderEvent calanderEvent = new CalanderEvent();

        calanderEvent.setEventId(calanderEventDTO.getEventId());
        calanderEvent.setTitle(calanderEventDTO.getTitle());
        calanderEvent.setStart(calanderEventDTO.getStart());
        calanderEvent.setEnd(calanderEventDTO.getEnd());
        calanderEvent.setAllDay(calanderEventDTO.getAllDay());

        calanderEvent.setEmployee(userRepository.findById(calanderEventDTO.getEmployeeId()).orElse(null));
        calanderEvent.setBusiness(businessRepository.findById(calanderEventDTO.getBusinessId()).orElse(null));
        calanderEvent.setProject(projectRepository.findById(calanderEventDTO.getProjectId()).orElse(null));

        return calanderEvent;
    }

}
