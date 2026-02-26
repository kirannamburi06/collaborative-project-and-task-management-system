package com.kiran.collaborativeprojectandtaskmanagementsystem.repository;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.ProjectMemberResponseDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.InvitationStatus;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Project;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.ProjectMember;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Users;
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
}
