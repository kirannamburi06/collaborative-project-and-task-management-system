package com.kiran.collaborativeprojectandtaskmanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
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

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Users invitedBy;

}
