package com.example.jwt.dtos.response.util;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagination {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalData;
}
