package com.kiran.collaborativeprojectandtaskmanagementsystem.repository;

import com.kiran.collaborativeprojectandtaskmanagementsystem.dto.ProjectResponseDTO;
import com.kiran.collaborativeprojectandtaskmanagementsystem.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {

    @Query("""
        select distinct p
        from Project p
        left join fetch p.members pm
        left join fetch pm.user u
        where u.username = :username
""")
    List<Project> getAllProjectsWithMembers(@Param("username") String username);
}
