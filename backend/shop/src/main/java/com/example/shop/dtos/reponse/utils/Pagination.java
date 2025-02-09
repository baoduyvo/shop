package com.example.shop.dtos.reponse.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
