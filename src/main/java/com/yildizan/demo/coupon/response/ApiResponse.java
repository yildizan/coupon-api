package com.yildizan.demo.coupon.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class ApiResponse {

    private final int code;
    private final String message;

}
