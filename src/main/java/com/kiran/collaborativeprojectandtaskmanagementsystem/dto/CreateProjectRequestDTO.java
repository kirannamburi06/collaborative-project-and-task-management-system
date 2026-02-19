package com.kiran.collaborativeprojectandtaskmanagementsystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateProjectRequestDTO {

    private String name;
    private String description;

}
