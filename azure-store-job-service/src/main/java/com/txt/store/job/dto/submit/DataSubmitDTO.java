package com.txt.store.job.dto.submit;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString
public class DataSubmitDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String objectId;
    private LocalDate receivedDate;
    private LocalDate expiredDate;
    private BigDecimal amount;
    private String keyNo;
    private String content;
    private String idNo;
    private String code;
    private String status;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String createBy;
    private String updateBy;
}
