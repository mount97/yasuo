package com.yasuo.dtos.common;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData<T> extends ResponseBase {
    private T responseData;

    @Builder
    public ResponseData(int code, String message, T responseData) {
        super.setCode(code);
        super.setMessage(message);
        this.responseData = responseData;
    }
}
