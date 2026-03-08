package com.kiran.collaborativeprojectandtaskmanagementsystem.repository;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.ProjectMemberProjection;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.ProjectMemberResponseDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.*;
import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.ProjectProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectMemberRepo extends JpaRepository<ProjectMember, Long> {

    boolean existsByProjectAndUser(Project project, Users user);

    ProjectMember findByProjectAndUser(Project project, Users user);

    List<ProjectMember> findByUserAndStatus(Users user, InvitationStatus status);

    @Query("""
        select new com.kiran.collaborativeprojectandtaskmanagementsystem.dto.ProjectMemberResponseDTO(
            pm.project.id,
            pm.project.name,
            pm.invitedBy.username,
            pm.role
        )
        from ProjectMember pm
        where pm.user = :user and pm.status = :status
""")
    List<ProjectMemberResponseDTO> findAllInvitationsDTO(@Param("user") Users user,
                                                         @Param("status") InvitationStatus status);

    @Query("""
        select p.id as id,
                p.name as name,
                p.description as description,
                p.createdAt as createdAt,
                p.createdBy.username as createdBy
        from ProjectMember pm
        join pm.project p
        where pm.user = :user
        and pm.status = :status
        and (:role is null or pm.role = :role)
""")
    Page<ProjectProjection> findProjectsForUser(@Param("user") Users user,
                                                @Param("status") InvitationStatus status,
                                                @Param("role") ProjectRole role,
                                                Pageable pageable);
    @Query("""
        select pm.project.id as projectId,
                pm.user.username as username
        from ProjectMember pm
        where pm.project.id in :projectIds
        and pm.status = 'ACTIVE'
        
""")
    List<ProjectMemberProjection> findMembersByProjectIds(@Param("projectIds") List<Long> projectIds);
}
