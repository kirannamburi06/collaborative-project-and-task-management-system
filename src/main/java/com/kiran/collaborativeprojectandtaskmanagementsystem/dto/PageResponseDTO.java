package com.kiran.collaborativeprojectandtaskmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDTO<T> {

    private List<T> content;
    private int currentPage;
    private int size;
    private long totalElements;
    private long totalPages;

}
