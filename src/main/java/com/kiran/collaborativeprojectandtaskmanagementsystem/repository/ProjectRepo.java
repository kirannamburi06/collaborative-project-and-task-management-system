package com.kiran.collaborativeprojectandtaskmanagementsystem.repository;

import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {

}
