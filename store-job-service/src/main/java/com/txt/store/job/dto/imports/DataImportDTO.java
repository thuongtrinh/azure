package com.txt.store.job.dto.imports;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DataImportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDate receivedDate;
    private LocalDate expiredDate;
    private BigDecimal amount;
    private String keyNo;
    private String content ;
    private String idNo;
    private String status;
    private String errorFields;

    @Override
    public String toString() {
        return "DataImportDTO{" +
                "receivedDate=" + receivedDate +
                ", expiredDate=" + expiredDate +
                ", amount=" + amount +
                ", keyNo='" + keyNo + '\'' +
                ", content='" + content + '\'' +
                ", idNo='" + idNo + '\'' +
                ", status='" + status + '\'' +
                ", errorFields='" + errorFields + '\'' +
                '}';
    }
}
