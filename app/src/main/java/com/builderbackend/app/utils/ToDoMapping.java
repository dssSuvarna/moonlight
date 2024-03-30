package com.builderbackend.app.utils;

import com.builderbackend.app.dtos.ToDoListDTO;
import com.builderbackend.app.dtos.ToDoListTaskDTO;
import com.builderbackend.app.repositories.BusinessRepository;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.repositories.ProjectRepository;
import com.builderbackend.app.models.ToDoList;
import com.builderbackend.app.models.ToDoListTask;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToDoMapping {

    @Autowired
    BusinessRepository businessRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProjectRepository projectRepository;

    /*
     * converts ToDoList Entity to ToDoListDTO
     */
    public ToDoListDTO convert_ToDoList_to_ToDoListDTO(ToDoList toDoList) {

        ToDoListDTO toDoListDTO = new ToDoListDTO();

        toDoListDTO.setToDoListId(toDoList.getToDoListId());
        toDoListDTO.setListName(toDoList.getListName());
        toDoListDTO.setUserId(toDoList.getUser().getUserId());
        toDoListDTO.setProjectId(toDoList.getProject().getProjectId());
        toDoListDTO.setBusinessId(toDoList.getBusiness().getBusinessId());
        List newList = new ArrayList<ToDoListTaskDTO>();

        for (ToDoListTask task : toDoList.getTaskList()) {
            newList.add(convert_ToDoListTask_to_ToDoListTaskDTO(task));
        }
        toDoListDTO.setTaskList(newList);
        // toDoListDTO.setTaskList() = new ArrayList<ToDoListTaskTransferObejct>();
        return toDoListDTO;
    }

    /*
     * converts ToDoListDTO to ToDoList Entity
     */
    public ToDoList convert_ToDoListDTO_to_ToDoList(ToDoListDTO toDoListDTO) {
        ToDoList toDoList = new ToDoList();
        toDoList.setToDoListId(toDoListDTO.getToDoListId());
        toDoList.setListName(toDoListDTO.getListName());
        toDoList.setUser((userRepository.getReferenceById(toDoListDTO.getUserId())));
        toDoList.setBusiness(toDoList.getUser().getBusiness());
        toDoList.setProject(projectRepository.getReferenceById(toDoListDTO.getProjectId()));
        List newList = new ArrayList<ToDoListTask>();

        for (ToDoListTaskDTO task : toDoListDTO.getTaskList()) {
            newList.add(convert_ToDoListTaskDTO_to_ToDoListTask(task));
        }
        toDoList.setTaskList(newList);
        return toDoList;
    }

    public ToDoListTaskDTO convert_ToDoListTask_to_ToDoListTaskDTO(ToDoListTask task) {
        ToDoListTaskDTO toDoListTaskDTO = new ToDoListTaskDTO();

        toDoListTaskDTO.setTaskId(task.getTaskId());
        toDoListTaskDTO.setTaskDescription(task.getTaskDescription());
        toDoListTaskDTO.setTaskDueDate(task.getTaskDueDate());
        toDoListTaskDTO.setStatus(task.getStatus());

        return toDoListTaskDTO;
    }

    public ToDoListTask convert_ToDoListTaskDTO_to_ToDoListTask(ToDoListTaskDTO toDoListTaskDTO) {
        ToDoListTask toDoListTask = new ToDoListTask();
        toDoListTask.setTaskId(toDoListTaskDTO.getTaskId());
        toDoListTask.setTaskDescription(toDoListTaskDTO.getTaskDescription());
        toDoListTask.setTaskDueDate(toDoListTaskDTO.getTaskDueDate());
        toDoListTask.setStatus(toDoListTaskDTO.getStatus());

        return toDoListTask;
    }

}
