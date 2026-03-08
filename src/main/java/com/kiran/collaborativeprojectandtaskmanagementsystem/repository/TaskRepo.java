package com.kiran.collaborativeprojectandtaskmanagementsystem.repository;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.TaskResponseDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Task;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

    @Query("""
        select new com.kiran.collaborativeprojectandtaskmanagementsystem.dto.TaskResponseDTO(
            t.id,
            t.name,
            t.description,
            t.status,
            assigned.username,
            creator.username,
            t.createdAt,
            t.updatedAt
        )
        from Task t
        left join t.assignedUser assigned
        join t.createdBy creator
        where t.project.id = :projectId
        and (:status is null or t.status = :status)
        and (:assignedTo is null or t.assignedUser.id = :assignedTo)
""")
    Page<TaskResponseDTO> getAllTasks(@Param("projectId") Long projectId,
                                      @Param("status") TaskStatus status,
                                      @Param("assignedTo") Long assignedTo,
                                      Pageable pageable);

}
