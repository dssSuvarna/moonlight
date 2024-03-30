package com.builderbackend.app.controllers;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.builderbackend.app.dtos.ToDoListDTO;
import com.builderbackend.app.services.ToDoListService;
import com.google.gson.Gson;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//todo: endpoint to delete the todolist
//todo: endpoint to delete a task
//todo: Create unit tests
//todo: exception handling
//todo: payload verification

@RestController
@RequestMapping("to-do-list")
public class ToDoController {

    private final ToDoListService toDoListService;

    @Autowired
    public ToDoController(ToDoListService toDoListService) {
        this.toDoListService = toDoListService;
    }

    @RequestMapping("/")
    public String main(String[] args) {

        System.out.println("yo");
        return "test";
    }

    @PostMapping("/create")
    public String createToDoListRequest(@RequestBody ToDoListDTO toDoList) {
        System.out.println("Starting ...");

        String response = convertToJson(toDoListService.saveToDoList(toDoList));
        return response;
    }

    // fix this error handling
    @GetMapping("/user-lists")
    public String getRequest(@RequestParam("projectId") String projectId) {
        // no need to create memory
        List<ToDoListDTO> allToDoLists;
        try {
            allToDoLists = toDoListService.getToDoListsForProjectId(projectId);
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

    // This will be used to modify existing todo list

    @PutMapping("/modify")
    public String modifyToDoList(@RequestBody ToDoListDTO toDoList) {

        ToDoListDTO response = toDoListService.modifyToDoList(toDoList);

        return convertToJson(response);
    }

    private String convertToJson(ToDoListDTO toDoListDTO) {
        return new Gson().toJson(toDoListDTO);
    }

    private String convertToJson(List<ToDoListDTO> allToDoLists) {
        return new Gson().toJson(allToDoLists);
    }

    /**
     * Delete a todo list
     * todo: figure out proper response for this. and for all other api requests
     */
    @DeleteMapping("/delete-list")
    public ResponseEntity<Void> deleteList(@RequestParam("toDoListId") String toDoListId) {
        if (!toDoListService.deleteList(toDoListId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Delete a todo list
     * todo: figure out proper response for this. and for all other api requests
     */
    @DeleteMapping("/delete-task")
    public ResponseEntity<Void> deleteTask(@RequestParam("toDoListTaskId") String toDoListTaskId) {
        if (!toDoListService.deleteTask(toDoListTaskId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /*
     * This endpoint checks to see if the todo list exists for the list id and
     * userId
     * 
     */
    @GetMapping("/exists")
    public ResponseEntity<?> doesListExist(@RequestParam String toDoListId, @RequestParam String userId) {
        String listId = toDoListService.findListIdByListNameAndUserId(toDoListId, userId);
        Map<String, Object> response = new HashMap<>();

        if (listId != null) {
            response.put("exists", true);
            response.put("listId", listId);
            return ResponseEntity.ok(response);
        }

        response.put("exists", false);
        return ResponseEntity.ok(response);
    }

    private String convertToJson(Object object) {
        return new Gson().toJson(object);
    }
}
