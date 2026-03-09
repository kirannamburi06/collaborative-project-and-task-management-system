package com.kiran.collaborativeprojectandtaskmanagementsystem.service;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.ActivityLogResponseDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.PageResponseDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.exception.InsufficientPrivilegesException;
import com.kiran.collaborativeprojectandtaskmanagementsystem.exception.ProjectNotFoundException;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.*;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.ActivityLogRepo;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.ProjectMemberRepo;
import com.kiran.collaborativeprojectandtaskmanagementsystem.repository.ProjectRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepo activityLogRepo;
    private final ProjectRepo projectRepo;
    private final ProjectMemberRepo projectMemberRepo;

    public void log(ActivityType activityType,
                    Users performedBy,
                    Project project,
                    EntityType entityType,
                    Long entityId
    ){
        ActivityLog activityLog = new ActivityLog();

        activityLog.setActivityType(activityType);
        activityLog.setProject(project);
        activityLog.setPerformedBy(performedBy);
        activityLog.setEntityType(entityType);
        activityLog.setEntityId(entityId);

        activityLogRepo.save(activityLog);
    }

    public PageResponseDTO<ActivityLogResponseDTO> getActivityLog(Long projectId,
                                                                  Pageable pageable,
                                                                  Users user) {

        Project project = projectRepo.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException("Project with id: " + projectId + " not found")
        );

        ProjectMember member = projectMemberRepo.findByProjectAndUser(project, user);
        if(member == null || member.getStatus() != InvitationStatus.ACTIVE) {
            throw new InsufficientPrivilegesException("You are not a member in this project");
        }

        Page<ActivityLogResponseDTO> activityLogs = activityLogRepo.getActivityLogs(projectId, pageable);

        return new PageResponseDTO<ActivityLogResponseDTO>(
                activityLogs.getContent(),
                activityLogs.getNumber(),
                activityLogs.getSize(),
                activityLogs.getTotalElements(),
                activityLogs.getTotalPages()
        );
    }
}
