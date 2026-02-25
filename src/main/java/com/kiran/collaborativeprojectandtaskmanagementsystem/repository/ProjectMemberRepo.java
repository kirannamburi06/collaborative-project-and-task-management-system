package com.kiran.collaborativeprojectandtaskmanagementsystem.repository;

import com.kiran.collaborativeprojectandtaskmanagementsystem.model.InvitationStatus;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Project;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.ProjectMember;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMemberRepo extends JpaRepository<ProjectMember, Long> {

    boolean existsByProjectAndUser(Project project, Users user);

    ProjectMember findByProjectAndUser(Project project, Users user);

    List<ProjectMember> findByUserAndStatus(Users user, InvitationStatus status);
}
