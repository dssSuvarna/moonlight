package com.builderbackend.app.clientPortal.services;

import lombok.Data;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.builderbackend.app.models.CalanderEvent;
import com.builderbackend.app.dtos.CalanderEventDTO;
import com.builderbackend.app.repositories.CalanderEventRepository;
import com.builderbackend.app.mappers.CalanderMapper;
import com.builderbackend.app.enums.EventType;

import com.builderbackend.app.services.NotificationService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Data

public class ClientCalandarEventService {

    @Autowired
    CalanderEventRepository calanderEventRepository;

    @Autowired
    CalanderMapper calanderMapper;

    @Autowired
    NotificationService notificationService;

    public List<CalanderEventDTO> getEventForProjectId(String projectId) {
        List<CalanderEventDTO> calanderEventDTOList = new ArrayList<>();

        List<CalanderEvent> calanderEventList = calanderEventRepository.findByProjectProjectId(projectId);

        if (calanderEventList.size() > 0) {
            for (CalanderEvent event : calanderEventList) {
                CalanderEventDTO calanderEventDTO = calanderMapper.convert_CalanderEvent_to_CalanderEventDTO(event);

                calanderEventDTOList.add(calanderEventDTO);

            }
        }

        notificationService.modifyNotificationViewedToTrue(projectId, EventType.EVENT_TYPE_6);

        return calanderEventDTOList;

    }

}
