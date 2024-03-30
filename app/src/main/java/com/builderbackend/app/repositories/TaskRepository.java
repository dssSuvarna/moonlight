package com.builderbackend.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.builderbackend.app.models.ToDoListTask;

@Repository
public interface TaskRepository extends JpaRepository<ToDoListTask, String> {

    ToDoListTask save(ToDoListTask toDoListTask);

    @Modifying
    @Query("UPDATE ToDoListTask n SET n.status = true WHERE n.taskId = :taskId")
    int updateStatusToTrue(@Param("taskId") String taskId);

}