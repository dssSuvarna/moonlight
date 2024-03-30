package com.builderbackend.app.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.builderbackend.app.dtos.CalanderEventDTO;
import com.builderbackend.app.services.CalanderEventService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("calander-event")
public class CalanderEventController {

    private final CalanderEventService calanderEventService;

    @Autowired
    public CalanderEventController(CalanderEventService calanderEventService) {
        this.calanderEventService = calanderEventService;
    }

    @PostMapping("/create")
    public ResponseEntity<CalanderEventDTO> createEvent(@RequestBody CalanderEventDTO calanderEventDTO) {
        CalanderEventDTO response = calanderEventService.createEvent(calanderEventDTO);
        return ResponseEntity.ofNullable(response);

    }

    @GetMapping("/get-event-for-projectId/{projectId}")
    public ResponseEntity<List<CalanderEventDTO>> getEventsForProject(@PathVariable String projectId) {
        List<CalanderEventDTO> calanderEventDTOList;

        calanderEventDTOList = calanderEventService.getEventForProjectId(projectId);
        return ResponseEntity.ofNullable(calanderEventDTOList);

    }

    @GetMapping("/get-event-for-userId/{userId}")
    public ResponseEntity<List<CalanderEventDTO>> getEventsForUser(@PathVariable String userId) {
        List<CalanderEventDTO> calanderEventDTOList;

        calanderEventDTOList = calanderEventService.getEventForUserId(userId);
        return ResponseEntity.ofNullable(calanderEventDTOList);

    }

    @GetMapping("/getEventsForDay")
    public ResponseEntity<List<CalanderEventDTO>> getEventsForCurrentToday(@RequestParam String userId,
            @RequestParam Instant start,
            @RequestParam Instant end) {
        List<CalanderEventDTO> calanderEventDTOList;

        calanderEventDTOList = calanderEventService.getEventForCurrentDay(userId, start, end);
        return ResponseEntity.ofNullable(calanderEventDTOList);

    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String eventId) {
        try {
            calanderEventService.deleteCalanderEvent(eventId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Corrected this line
        }
    }

}
