package com.txt.store.job.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HeaderTemplateDTO {

    private String objectId;
    private String code;
    private String name;
    private List<String> headerNames;
}
