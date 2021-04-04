package com.yildizan.demo.coupon.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.yildizan.demo.coupon.entity.Coupon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final int THREAD_COUNT = 4;
    private static final int COUPON_COUNT = 50;
    private static final int REQUEST_COUNT = 100;

    @Test
    public void whenValid_shouldSave() throws Exception {
        Coupon coupon = new Coupon();
        coupon.setCount(1);
        coupon.setDiscount(1.0);

        mockMvc.perform(post("/coupon/save")
                .content(objectMapper.writeValueAsString(coupon))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenInvalidCount_expectError() throws Exception {
        Coupon coupon = new Coupon();
        coupon.setCount(-1);    // invalid property
        coupon.setDiscount(1.0);

        mockMvc.perform(post("/coupon/save")
                .content(objectMapper.writeValueAsString(coupon))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenInvalidDiscount_expectError() throws Exception {
        Coupon coupon = new Coupon();
        coupon.setCount(1);
        coupon.setDiscount(-1.0);   // invalid property

        mockMvc.perform(post("/coupon/save")
                .content(objectMapper.writeValueAsString(coupon))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenMultiUsers_expectSomeErrors() throws Exception {
        Coupon coupon = new Coupon();
        coupon.setCount(COUPON_COUNT);

        // insert sample coupon
        MvcResult result = mockMvc.perform(post("/coupon/save")
                .content(objectMapper.writeValueAsString(coupon))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        int id = JsonPath.read(result.getResponse().getContentAsString(), "$.result.id");

        final AtomicInteger assigned = new AtomicInteger();
        final Executor executor = Executors.newFixedThreadPool(THREAD_COUNT);

        // make parallel requests
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for(int i = 0; i < REQUEST_COUNT; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> {
                try {
                    int status = mockMvc.perform(get("/coupon/assign/" + id)
                            .accept(MediaType.APPLICATION_JSON))
                            .andReturn()
                            .getResponse()
                            .getStatus();
                    if(HttpStatus.valueOf(status) == HttpStatus.OK) {
                        assigned.incrementAndGet();
                    }
                }
                catch (Exception ignored) {}
                return null;
            }, executor));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[REQUEST_COUNT])).join();

        assertEquals(COUPON_COUNT, assigned.get());
    }

}
