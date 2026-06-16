package com.kiran.collaborativeprojectandtaskmanagementsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_proofs")
@Getter
@Setter
@NoArgsConstructor
public class TaskProof {

    @Id @GeneratedValue
    private Long id;

    private String imageUrl;
    private String publicId;

    private LocalDateTime uploadedAt;

    @ManyToOne
    private Users uploadedBy;

    @ManyToOne
    private Task task;

}
