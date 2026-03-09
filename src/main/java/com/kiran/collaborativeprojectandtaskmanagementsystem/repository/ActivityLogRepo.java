package com.kiran.collaborativeprojectandtaskmanagementsystem.repository;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.ActivityLogResponseDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ActivityLogRepo extends JpaRepository<ActivityLog, Long> {

    @Query("""
        select new com.kiran.collaborativeprojectandtaskmanagementsystem.dto.ActivityLogResponseDTO(
            al.activityType,
            al.performedBy.username,
            al.project.id,
            al.entityType,
            al.entityId,
            al.createdAt
        )
        from ActivityLog al
        where al.project.id = :projectId
""")
    Page<ActivityLogResponseDTO> getActivityLogs(Long projectId, Pageable pageable);
}
