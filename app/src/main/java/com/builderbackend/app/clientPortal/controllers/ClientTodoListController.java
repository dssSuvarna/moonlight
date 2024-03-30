package com.builderbackend.app.clientPortal.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.builderbackend.app.dtos.ToDoListDTO;
import com.builderbackend.app.clientPortal.services.ClientTodoListService;
import com.google.gson.Gson;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//todo: endpoint to delete the todolist
//todo: endpoint to delete a task
//todo: Create unit tests
//todo: exception handling
//todo: payload verification

@RestController
@RequestMapping("client-todo-list")
public class ClientTodoListController {

    private final ClientTodoListService clientTodoListService;

    @Autowired
    public ClientTodoListController(ClientTodoListService clientTodoListService) {
        this.clientTodoListService = clientTodoListService;
    }

    @GetMapping("/user-lists")
    public String getRequest(@RequestParam("projectId") String projectId) {
        // no need to create memory
        List<ToDoListDTO> allToDoLists;
        try {
            allToDoLists = clientTodoListService.getToDoListsForProjectId(projectId);
        } catch (Exception e) {
            // probably shouldn't just return the messgae tbh..
            // should log the error and should return a logical response
            return e.getMessage();
        }

        if (allToDoLists.size() == 0) {
            return "{}";
        }

        String response = convertToJson(allToDoLists);

        return response;
    }

    @PutMapping("/confirm-task/{projectId}/{todoListTaskId}")
    public ResponseEntity<Void> confirmTask(@PathVariable String projectId, @PathVariable String todoListTaskId) {
        try {
            clientTodoListService.confirmTodoListTask(projectId, todoListTaskId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Corrected this line
        }
    }

    private String convertToJson(List<ToDoListDTO> allToDoLists) {
        return new Gson().toJson(allToDoLists);
    }

}
