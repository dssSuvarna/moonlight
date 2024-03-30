package com.builderbackend.app.models;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Data
@Entity
public class ToDoListTask {

    @Id
    String taskId;
    String taskDescription;
    String taskDueDate;
    Boolean status;
    @ManyToOne
    @JoinColumn(name = "toDoList_id", nullable = false)
    @JsonBackReference
    ToDoList toDoList;
}
