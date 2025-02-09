package com.example.shop.dtos.reponse.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaginationDTO {
    private Pagination pagination;
    private Object result;

}
