package com.builderbackend.app.clientPortal.services;

import java.lang.Exception;
import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.builderbackend.app.models.ToDoList;
import com.builderbackend.app.utils.ToDoMapping;

import jakarta.transaction.Transactional;

import com.builderbackend.app.dtos.ToDoListDTO;
import com.builderbackend.app.repositories.BusinessRepository;
import com.builderbackend.app.repositories.TaskRepository;
import com.builderbackend.app.repositories.ToDoListRepository;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.enums.EventType;
//import com.builderbackend.app.map.ToDoListMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ClientTodoListService {

    @Autowired
    ToDoListRepository toDoListRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    ToDoMapping toDoMapping;

    @Autowired
    ClientNotificationService notificationService;

    public List<ToDoListDTO> getToDoListsForProjectId(String projectId) throws Exception {

        List<ToDoList> newToDoLists = new ArrayList<ToDoList>();
        try {
            newToDoLists = toDoListRepository.findByProjectProjectId(projectId);

        } catch (Exception e) {
            // should probably log this
            System.out.println("Exception: " + e);
            throw e;
        }

        List<ToDoListDTO> allToDoListsDTOs = new ArrayList<ToDoListDTO>();

        for (ToDoList toDoListEntity : newToDoLists) {
            allToDoListsDTOs.add(toDoMapping.convert_ToDoList_to_ToDoListDTO(toDoListEntity));
        }

        notificationService.modifyNotificationViewedToTrue(projectId, EventType.EVENT_TYPE_3);

        return allToDoListsDTOs;
    }

    @Transactional
    public void confirmTodoListTask(String projectId, String todoListTaskId) {
        taskRepository.updateStatusToTrue(todoListTaskId);
        notificationService.createNotification(projectId, todoListTaskId, EventType.EVENT_TYPE_9);
    }
}
