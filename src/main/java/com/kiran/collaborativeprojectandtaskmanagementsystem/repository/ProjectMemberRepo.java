package com.kiran.collaborativeprojectandtaskmanagementsystem.repository;

import com.kiran.collaborativeprojectandtaskmanagementsystem.model.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepo extends JpaRepository<ProjectMember, Long> {
}
