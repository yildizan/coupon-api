package com.yildizan.demo.coupon.controller;

import com.yildizan.demo.coupon.constant.SuccessMessage;
import com.yildizan.demo.coupon.entity.Coupon;
import com.yildizan.demo.coupon.exception.ApiException;
import com.yildizan.demo.coupon.response.ApiResponse;
import com.yildizan.demo.coupon.response.ResponseBuilder;
import com.yildizan.demo.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping(path = "/list")
    public ApiResponse list() {
        return ResponseBuilder.success(couponService.list());
    }

    @PostMapping(path = "/save")
    public ApiResponse save(@Valid @RequestBody Coupon coupon) {
        String message = coupon.getId() == 0 ? SuccessMessage.CREATED : SuccessMessage.UPDATED;
        return ResponseBuilder.success(message, couponService.save(coupon));
    }

    @DeleteMapping(path = "/delete/{id}")
    public ApiResponse delete(@PathVariable int id) {
        couponService.delete(id);
        return ResponseBuilder.success(SuccessMessage.DELETED);
    }

    @Transactional
    @GetMapping(path = "/assign/{id}")
    public ApiResponse assign(@PathVariable int id) throws ApiException {
        return ResponseBuilder.success(String.format(SuccessMessage.ASSIGNED, couponService.assign(id).getName()));
    }

}
