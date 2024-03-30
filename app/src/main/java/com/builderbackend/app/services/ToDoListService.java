package com.builderbackend.app.services;

import java.lang.Exception;
import java.util.UUID;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.builderbackend.app.models.ToDoList;
import com.builderbackend.app.models.ToDoListTask;
import com.builderbackend.app.utils.ToDoMapping;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import com.builderbackend.app.dtos.ToDoListDTO;
import com.builderbackend.app.dtos.ToDoListTaskDTO;
import com.builderbackend.app.repositories.BusinessRepository;
import com.builderbackend.app.repositories.TaskRepository;
import com.builderbackend.app.repositories.ToDoListRepository;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.enums.EventType;
//import com.builderbackend.app.map.ToDoListMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ToDoListService {

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
    NotificationService notificationService;

    // todo: error handling and throwing exceptions
    public ToDoListDTO saveToDoList(ToDoListDTO toDoList) {

        // Setting UUID for to do list
        String listId = UUID.randomUUID().toString();
        toDoList.setToDoListId(listId);

        // Setting UUIDs for todolistTask's
        for (ToDoListTaskDTO task : toDoList.getTaskList()) {
            String taskId = UUID.randomUUID().toString();
            task.setTaskId(taskId);
            notificationService.createNotification(toDoList.getProjectId(), taskId, EventType.EVENT_TYPE_3);

        }

        // converting toDoList dto to entity
        ToDoList toDoListEntity = toDoMapping.convert_ToDoListDTO_to_ToDoList(toDoList);

        // this is needed to link a task to a todolist
        // our mapper we used doesnt do this for us
        for (ToDoListTask task : toDoListEntity.getTaskList()) {
            task.setToDoList(toDoListEntity);
        }

        System.out.println("Saving list...");
        toDoListRepository.save(toDoListEntity);
        System.out.println("Done. ");

        return toDoList;
    }

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

        notificationService.modifyNotificationViewedToTrue(projectId, EventType.EVENT_TYPE_9);

        return allToDoListsDTOs;
    }

    @Transactional
    public ToDoListDTO modifyToDoList(ToDoListDTO toDoList) {

        Optional<ToDoList> existingToDoListOpt = toDoListRepository.findById(toDoList.getToDoListId());
        if (!existingToDoListOpt.isPresent()) {
            throw new EntityNotFoundException("ToDoList not found with id: " + toDoList.getToDoListId());
        }
        ToDoList existingToDoList = existingToDoListOpt.get();

        for (ToDoListTask task : existingToDoList.getTaskList()) {
            taskRepository.deleteById(task.getTaskId());

        }
        existingToDoList.getTaskList().clear();

        ToDoListDTO toDoListDTO = toDoMapping.convert_ToDoList_to_ToDoListDTO(existingToDoList);

        // Setting UUIDs for todolistTask's
        for (ToDoListTaskDTO task : toDoList.getTaskList()) {
            if (task.getTaskId() == null) {
                String taskId = UUID.randomUUID().toString();
                task.setTaskId(taskId);
                toDoListDTO.getTaskList().add(task);
                notificationService.createNotification(toDoList.getProjectId(), taskId, EventType.EVENT_TYPE_3);
            }
        }

        // converting toDoList dto to entity
        ToDoList toDoListEntity = toDoMapping.convert_ToDoListDTO_to_ToDoList(toDoListDTO);

        // this is needed to link a task to a todolist
        // our mapper we used doesnt do this for us
        for (ToDoListTask task : toDoListEntity.getTaskList()) {
            if (task.getToDoList() == null) {
                task.setToDoList(toDoListEntity);
            }
        }

        System.out.println("Saving list...");
        toDoListRepository.save(toDoListEntity);
        System.out.println("Done. ");

        return toDoList;
    }

    public boolean deleteList(String toDoListId) {

        try {
            toDoListRepository.deleteById(toDoListId);
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

        return true;
    }

    public boolean deleteTask(String toDoListTaskId) {

        try {
            taskRepository.deleteById(toDoListTaskId);
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

        return true;
    }

    public String findListIdByListNameAndUserId(String toDoListId, String userId) {
        ToDoList existingToDoList = toDoListRepository.findBytoDoListIdAndUserUserId(toDoListId, userId);
        if (existingToDoList != null) {
            return existingToDoList.getToDoListId();
        }
        return null;
    }

}