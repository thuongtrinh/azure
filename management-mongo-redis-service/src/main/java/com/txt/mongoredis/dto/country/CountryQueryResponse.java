package com.txt.mongoredis.dto.country;

import com.txt.mongoredis.dto.common.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class CountryQueryResponse extends ResponseDTO<CountryDTO> {

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}