package com.txt.store.job.dto.submit;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class SearchSubmitDataDTO {
    private static final long serialVersionUID = 1L;

    private String fromDate;
    private String toDate;
    private String code;
    private String status;
}
