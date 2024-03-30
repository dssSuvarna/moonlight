package com.builderbackend.app.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.builderbackend.app.models.ToDoList;

@Repository
public interface ToDoListRepository extends JpaRepository<ToDoList, String> {
    // Weird method name is because the user id column in the todo list db is called
    // "user_user_id"
    // this is beacause i never specified naming conventions when defining my
    // ManyToOne mappings ...
    List<ToDoList> findByUserUserId(String userId);

    List<ToDoList> findByProjectProjectId(String projectId);

    ToDoList findBytoDoListIdAndUserUserId(String toDoListId, String userId);

    ToDoList save(ToDoList toDoList);
}
