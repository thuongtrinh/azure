package com.txt.store.job.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HeaderTemplateDTO {

    private String objectId;
    private String code;
    private String name;
    private List<String> headerNames;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
