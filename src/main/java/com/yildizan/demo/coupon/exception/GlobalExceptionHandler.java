package com.yildizan.demo.coupon.exception;

import com.yildizan.demo.coupon.response.ResponseBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // validation error
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder message = new StringBuilder();
        for(ObjectError error : e.getBindingResult().getAllErrors()) {
            message.append(error.getDefaultMessage()).append("\n");
        }
        return build(HttpStatus.BAD_REQUEST, message.toString());
    }

    // api error
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handle(ApiException e) {
        return build(e.getStatus(), e.getMessage());
    }

    // non-predefined error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception e) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    private ResponseEntity<Object> build(HttpStatus status, String message) {
        return new ResponseEntity<>(ResponseBuilder.error(status.value(), message), status);
    }

}
