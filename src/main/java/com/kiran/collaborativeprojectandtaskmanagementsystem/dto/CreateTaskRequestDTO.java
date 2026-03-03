package com.kiran.collaborativeprojectandtaskmanagementsystem.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateTaskRequestDTO {

    @NonNull
    private String name;

    private String description;

    private Long assignedUserId;

}
