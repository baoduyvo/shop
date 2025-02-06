package com.example.jwt.dtos.response.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaginationDTO {
    private Pagination pagination;
    private Object result;

}
