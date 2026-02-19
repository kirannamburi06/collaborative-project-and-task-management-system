package com.kiran.collaborativeprojectandtaskmanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "project_members")
public class ProjectMember {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private Project project;

    @ManyToOne(optional = false)
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status;

    @Column(updatable = false)
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss a")
    private LocalDateTime joinedAt;

}
