package com.yildizan.demo.coupon.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
class SuccessResponse<T> extends ApiResponse {

    private final T result;

}
