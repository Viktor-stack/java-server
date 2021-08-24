package com.rujavacours.business.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private String title;
    private Integer completed;
    private Long categoryId;
    private Long priorityId;
    private String email;
    private Date dateFrom;
    private Date dateTo;


    private Integer pageNumber;
    private Integer pageSize;

    private String sortColumn;
    private String sortDirection;


}
