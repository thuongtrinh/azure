package com.txt.store.job.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionOps<T> {
    private static final long serialVersionUID = -6269795830589816782L;
    private String exchangeId;
    private String source;
    private String eventKey = "triggerEvent";
    private String transactionType;
    private String correlationId;
    private String vip;
    private String authenOption;
    private String primaryPolicyNo;
    private String clientId;
    private String officeCode;
    private String createdDate;
    private String createdBy;
    private T payload;
    private String idNumber;
    private String location;
    private String transType;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
