package com.sky.controller.admin;


import com.sky.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("AdminShopController")
@Slf4j
@ApiOperation(value="商铺管理")
@RequestMapping("/admin/shop")
public class ShopController {


    private static final String SHOP_KEY = "shop_status";
    @Autowired
    private RedisTemplate redisTemplate;


    @PutMapping("/{status}")
    public Result setStatus(@PathVariable Integer status){

        log.info("商铺状态：{}", status);
        redisTemplate.opsForValue().set(SHOP_KEY, status);

        return Result.success();
    }


    @GetMapping("/status")
    public Result getStatus(){
        Integer o = (Integer) redisTemplate.opsForValue().get(SHOP_KEY);
        return Result.success(o);
    }

}
