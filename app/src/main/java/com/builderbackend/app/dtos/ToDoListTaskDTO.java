package com.builderbackend.app.dtos;

import lombok.Data;

@Data
public class ToDoListTaskDTO {
    String taskId;
    String taskDescription;
    String taskDueDate;
    Boolean status;
}
