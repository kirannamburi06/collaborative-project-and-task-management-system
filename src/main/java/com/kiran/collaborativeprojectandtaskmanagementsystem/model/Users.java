package com.kiran.collaborativeprojectandtaskmanagementsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ProjectMember> projects = new ArrayList<>();

    @OneToMany(mappedBy = "assignedUser", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Task> assignedTasks = new ArrayList<>();
}
