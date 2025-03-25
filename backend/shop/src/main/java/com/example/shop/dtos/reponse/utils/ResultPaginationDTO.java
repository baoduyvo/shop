package com.example.shop.dtos.reponse.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultPaginationDTO {
    private Pagination pagination;
    private Object result;

}
