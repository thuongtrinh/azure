package com.txt.store.job.entities.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Document(collection = "data_import")
public class DataImport {

    private ObjectId _id;
    private LocalDate receivedDate;
    private LocalDate expriredDate;
    private BigDecimal amount;
    private String keyNo;
    private String content ;
    private String idNumber;
    private String code;
    private String status;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String createBy;
    private String updateBy;
}
