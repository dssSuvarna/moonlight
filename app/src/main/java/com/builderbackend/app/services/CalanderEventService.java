package com.builderbackend.app.services;

import lombok.Data;
import java.lang.Exception;
import java.util.UUID;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.builderbackend.app.models.CalanderEvent;
import com.builderbackend.app.dtos.CalanderEventDTO;
import com.builderbackend.app.repositories.CalanderEventRepository;
import com.builderbackend.app.mappers.CalanderMapper;
import com.builderbackend.app.enums.EventType;

import java.time.Instant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Data
public class CalanderEventService {

    @Autowired
    CalanderEventRepository calanderEventRepository;

    @Autowired
    CalanderMapper calanderMapper;

    @Autowired
    NotificationService notificationService;

    public CalanderEventDTO createEvent(CalanderEventDTO calanderEventDTO) {

        // Setting UUID for event
        String eventId = UUID.randomUUID().toString();
        calanderEventDTO.setEventId(eventId);

        CalanderEvent calanderEvent = calanderMapper.convert_CalanderEventDTO_to_CalanderEvent(calanderEventDTO);
        calanderEventRepository.save(calanderEvent);

        notificationService.createNotification(calanderEventDTO.getProjectId(), eventId,
                EventType.EVENT_TYPE_6);

        return calanderEventDTO;

    }

    public List<CalanderEventDTO> getEventForProjectId(String projectId) {
        List<CalanderEventDTO> calanderEventDTOList = new ArrayList<>();

        List<CalanderEvent> calanderEventList = calanderEventRepository.findByProjectProjectId(projectId);

        if (calanderEventList.size() > 0) {
            for (CalanderEvent event : calanderEventList) {
                CalanderEventDTO calanderEventDTO = calanderMapper.convert_CalanderEvent_to_CalanderEventDTO(event);

                calanderEventDTOList.add(calanderEventDTO);

            }
        }

        return calanderEventDTOList;

    }

    public List<CalanderEventDTO> getEventForUserId(String userId) {
        List<CalanderEventDTO> calanderEventDTOList = new ArrayList<>();

        List<CalanderEvent> calanderEventList = calanderEventRepository.findByEmployeeUserId(userId);

        if (calanderEventList.size() > 0) {
            for (CalanderEvent event : calanderEventList) {
                CalanderEventDTO calanderEventDTO = calanderMapper.convert_CalanderEvent_to_CalanderEventDTO(event);

                calanderEventDTOList.add(calanderEventDTO);

            }
        }

        return calanderEventDTOList;

    }

    public List<CalanderEventDTO> getEventForCurrentDay(String userId, Instant start, Instant end) {

        // ZoneId utcZoneId = ZoneId.of("UTC");
        // Instant nowInstant = Instant.now(); // Current time in UTC
        // ZonedDateTime nowUtc = ZonedDateTime.ofInstant(nowInstant, utcZoneId);

        // // Start of the current day in UTC
        // Instant startInstant = nowUtc.truncatedTo(ChronoUnit.DAYS).toInstant();

        // // End of the current day in UTC
        // Instant endInstant =
        // nowUtc.plusDays(1).truncatedTo(ChronoUnit.DAYS).toInstant();

        // System.out.println("123**************");

        // System.out.println(startInstant);
        // System.out.println(endInstant);

        List<CalanderEvent> todaysEvents = calanderEventRepository.findByEmployeeUserIdAndStartBetween(userId, start,
                end);

        List<CalanderEventDTO> calanderEventDTOList = new ArrayList<>();

        if (todaysEvents.size() > 0) {
            for (CalanderEvent event : todaysEvents) {
                CalanderEventDTO calanderEventDTO = calanderMapper.convert_CalanderEvent_to_CalanderEventDTO(event);

                calanderEventDTOList.add(calanderEventDTO);

            }
        }

        return calanderEventDTOList;

    }

    public void deleteCalanderEvent(String eventId) throws Exception {
        Optional<CalanderEvent> calandarEventOptional = calanderEventRepository.findById(eventId);

        try {
            // Delete all the files first
            if (calandarEventOptional.isPresent()) {
                calanderEventRepository.deleteById(eventId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file ");

        }

    }

}
