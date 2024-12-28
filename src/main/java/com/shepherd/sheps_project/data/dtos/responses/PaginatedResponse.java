package com.shepherd.sheps_project.data.dtos.responses;

import lombok.*;

import java.util.List;

@Builder
@Getter
public class PaginatedResponse<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private boolean last;
}
