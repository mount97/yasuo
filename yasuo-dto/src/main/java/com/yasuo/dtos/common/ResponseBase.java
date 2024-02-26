package com.yasuo.dtos.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ResponseBase {
    private int code;
    private String message;
}
