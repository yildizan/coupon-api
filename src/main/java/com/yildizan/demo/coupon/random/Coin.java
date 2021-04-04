package com.yildizan.demo.coupon.random;

public final class Coin {

    private Coin() {}

    public static final int HEADS = 1;
    public static final int TAILS = 2;

    public static int flip() {
        return Math.random() < 0.5 ? HEADS : TAILS;
    }

}
