package com.example.sqljpasqlserver.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObject<T> {
    private Boolean successfull;
    private T result;
    private Integer errorCode;
    private String errorMessage;

}
