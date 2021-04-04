package com.yildizan.demo.coupon.repository;

import com.yildizan.demo.coupon.entity.User;
import com.yildizan.demo.coupon.random.Coin;
import org.springframework.stereotype.Component;

@Component
public class UserRepository {

    public User generate() {
        int random = Coin.flip();
        if(random == Coin.HEADS) {
            return new User(1, "John Doe");
        }
        else if(random == Coin.TAILS) {
            return new User(2, "Jane Doe");
        }
        else {
            return new User(0, "GG Math");
        }
    }

}
