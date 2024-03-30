package com.builderbackend.app.dtos;

import lombok.Data;
import java.util.List;

@Data
public class ToDoListDTO {

    private String toDoListId;
    private String listName;
    private String projectId;
    private String userId; // Link the ToDoList to a User (client the the list is intended for)
    private String businessId; // Link the ToDoList to a Business (business who is creating/assigning the list)
    List<ToDoListTaskDTO> taskList;

}