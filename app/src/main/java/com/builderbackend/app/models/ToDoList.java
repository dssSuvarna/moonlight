package com.builderbackend.app.models;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ToDoList {

    @Id
    String toDoListId;
    String listName;

    @ManyToOne
    private User user; // Link the ToDoList to a User (client the list is intended for)

    @ManyToOne
    private Project project;

    @ManyToOne
    private Business business; // Link the ToDoList to a Business (business who is creating/assigning the list)

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "toDoList")
    @JsonManagedReference
    List<ToDoListTask> taskList;
}