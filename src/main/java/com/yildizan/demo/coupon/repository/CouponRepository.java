package com.yildizan.demo.coupon.repository;

import com.yildizan.demo.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CouponRepository extends CrudRepository<Coupon, Integer> {

    @Override
    List<Coupon> findAll();

    @Modifying
    @Query("update Coupon a set a.count = a.count - 1 where a.id = :id and a.count > 0 ")
    int decrementCountById(int id);

}
