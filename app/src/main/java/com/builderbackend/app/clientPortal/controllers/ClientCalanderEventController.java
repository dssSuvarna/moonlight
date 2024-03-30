package com.builderbackend.app.clientPortal.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.builderbackend.app.dtos.CalanderEventDTO;
import com.builderbackend.app.clientPortal.services.ClientCalandarEventService;

import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("client-calander-event")
public class ClientCalanderEventController {

    private final ClientCalandarEventService clientCalandarEventService;

    @Autowired
    public ClientCalanderEventController(ClientCalandarEventService clientCalandarEventService) {
        this.clientCalandarEventService = clientCalandarEventService;
    }

    @GetMapping("/get-event-for-projectId/{projectId}")
    public ResponseEntity<List<CalanderEventDTO>> getEventsForProject(@PathVariable String projectId) {
        List<CalanderEventDTO> calanderEventDTOList;

        calanderEventDTOList = clientCalandarEventService.getEventForProjectId(projectId);
        return ResponseEntity.ofNullable(calanderEventDTOList);

    }

}
