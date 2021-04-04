package com.yildizan.demo.coupon.entity;

import com.yildizan.demo.coupon.constant.ErrorMessage;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;

@Data
@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Min(value = 0, message = ErrorMessage.INVALID_COUNT)
    private int count;

    @Min(value = 0, message = ErrorMessage.INVALID_DISCOUNT)
    private double discount;

}
