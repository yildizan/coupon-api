package com.yildizan.demo.coupon.test;

import com.yildizan.demo.coupon.constant.ErrorMessage;
import com.yildizan.demo.coupon.entity.Coupon;
import com.yildizan.demo.coupon.entity.User;
import com.yildizan.demo.coupon.exception.ApiException;
import com.yildizan.demo.coupon.repository.CouponRepository;
import com.yildizan.demo.coupon.repository.UserRepository;
import com.yildizan.demo.coupon.service.CouponService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CouponService couponService;

    @Test
    public void whenList_expectSizeOfTwo() {
        when(couponRepository.findAll()).thenReturn(Arrays.asList(new Coupon(), new Coupon()));

        assertEquals(2, couponService.list().size());
    }

    @Test
    public void whenSave_shouldSuccess() {
        when(couponRepository.save(any(Coupon.class))).then((Answer<Coupon>) invocationOnMock -> {
                Coupon coupon = invocationOnMock.getArgument(0);
                coupon.setId(coupon.getId() + 1);
                return coupon;
        });

        assertTrue(couponService.save(new Coupon()).getId() > 0);
    }

    @Test
    public void whenDelete_shouldSuccess() {
        couponService.delete(1);
        verify(couponRepository, times(1)).deleteById(1);
    }

    @Test
    public void whenAssignActive_shouldSuccess() {
        int id = 1;
        User user = new User(id, "Doe");

        when(couponRepository.existsById(id)).thenReturn(true);
        when(couponRepository.decrementCountById(id)).thenReturn(1);
        when(userRepository.generate()).thenReturn(user);

        assertEquals(user, couponService.assign(id));
    }

    @Test
    public void whenAssignNonExisting_expectError() {
        int id = 1;

        when(couponRepository.existsById(id)).thenReturn(false);

        ApiException e = assertThrows(ApiException.class, () -> couponService.assign(id));
        assertEquals(String.format(ErrorMessage.NOT_FOUND, id), e.getMessage());
    }

    @Test
    public void whenAssignInactive_expectError() {
        int id = 1;

        when(couponRepository.existsById(id)).thenReturn(true);
        when(couponRepository.decrementCountById(id)).thenReturn(0);

        ApiException e = assertThrows(ApiException.class, () -> couponService.assign(id));
        assertEquals(ErrorMessage.INACTIVE_COUPON, e.getMessage());
    }

}
