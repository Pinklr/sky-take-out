package com.sky.controller.user;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    @PostMapping ("/add")
    private Result addCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("shoppingCartDTO:{}", shoppingCartDTO);
        shoppingCartService.addCart(shoppingCartDTO);
        return Result.success();

    }

    @GetMapping ("/list")
    private Result list() {
        List<ShoppingCart> list = shoppingCartService.list();
        return Result.success(list);
    }

    @DeleteMapping ("/clean")
    private Result clean() {
        shoppingCartService.clean();
        log.info("清空购物车成功");
        return Result.success();
    }
}
