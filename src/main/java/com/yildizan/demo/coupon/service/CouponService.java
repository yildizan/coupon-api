package com.yildizan.demo.coupon.service;

import com.yildizan.demo.coupon.constant.ErrorMessage;
import com.yildizan.demo.coupon.entity.Coupon;
import com.yildizan.demo.coupon.entity.User;
import com.yildizan.demo.coupon.exception.ApiException;
import com.yildizan.demo.coupon.repository.CouponRepository;
import com.yildizan.demo.coupon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;

    public List<Coupon> list() {
        return couponRepository.findAll();
    }

    public Coupon save(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public void delete(int id) {
        couponRepository.deleteById(id);
    }

    @Transactional
    public User assign(int id) throws ApiException {
        // not exists
        if(!couponRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, String.format(ErrorMessage.NOT_FOUND, id));
        }
        // valid
        else if(couponRepository.decrementCountById(id) > 0) {
            return userRepository.generate();
        }
        // insufficient count
        else {
            throw new ApiException(HttpStatus.FORBIDDEN, ErrorMessage.INACTIVE_COUPON);
        }
    }

}
