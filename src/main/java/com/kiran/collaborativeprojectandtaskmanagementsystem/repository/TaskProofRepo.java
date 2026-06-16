package com.kiran.collaborativeprojectandtaskmanagementsystem.repository;

import com.kiran.collaborativeprojectandtaskmanagementsystem.model.TaskProof;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskProofRepo extends JpaRepository<TaskProof, Long> {

    List<TaskProof> findByTaskId(Long taskId);

}
