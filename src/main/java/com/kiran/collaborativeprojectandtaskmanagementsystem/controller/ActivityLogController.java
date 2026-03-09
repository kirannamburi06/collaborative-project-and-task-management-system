package com.kiran.collaborativeprojectandtaskmanagementsystem.controller;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.ActivityLogResponseDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.PageResponseDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Users;
import com.kiran.collaborativeprojectandtaskmanagementsystem.security.UserPrincipal;
import com.kiran.collaborativeprojectandtaskmanagementsystem.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects/{projectId}/activity")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    @GetMapping
    public PageResponseDTO<ActivityLogResponseDTO> getActivityLog(
            @PathVariable Long projectId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                                                                      Pageable pageable,
            @AuthenticationPrincipal UserPrincipal userPrincipal){

        Users user = userPrincipal.getUser();
        return activityLogService.getActivityLog(projectId, pageable, user);
    }

}
