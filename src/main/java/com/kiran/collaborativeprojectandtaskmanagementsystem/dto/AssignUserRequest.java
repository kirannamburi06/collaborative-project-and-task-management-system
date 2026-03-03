package com.kiran.collaborativeprojectandtaskmanagementsystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AssignUserRequest {

    @NonNull
    private Long assignUserId;

}
