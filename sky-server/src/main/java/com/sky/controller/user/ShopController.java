package com.sky.controller.user;


import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.naming.Name;

@RestController("UserShopController")
@Slf4j
@Api(tags="商铺接口")
@RequestMapping("/user/shop")
public class ShopController {


    private static final String SHOP_KEY = "shop_status";
    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping("/status")
    public Result getStatus(){
        Integer o = (Integer) redisTemplate.opsForValue().get(SHOP_KEY);
        return Result.success(o);
    }



}
