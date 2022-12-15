package com.txt.store.job.dto.imports;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class DataFileImportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    //Field in excel file
    private LocalDate receivedDate;
    private LocalDate creditDate;
    private BigDecimal amount;
    private String refNo;
    private String paymentReason ;
    private String primaryPolicyNo;
    private String bankCode;

    //Field added
    private String status;
    private String errorFields;
    private String sourceData;
}
