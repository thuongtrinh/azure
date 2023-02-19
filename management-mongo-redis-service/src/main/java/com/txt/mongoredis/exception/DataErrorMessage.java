package com.txt.mongoredis.exception;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor()
public class DataErrorMessage {

    @NonNull
    private int statusCode;
    private Date timestamp;

    @NonNull
    private String message;
    private String description;
}