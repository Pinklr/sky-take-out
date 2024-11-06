package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void addCart(ShoppingCartDTO shoppingCartDTO) {
        //首先判断购物车中是否包含该菜品或套餐，如果有，则数量加1，如果没有，则添加到购物车中
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.select(shoppingCartDTO);
        log.info("shoppingCarts:{}",shoppingCarts);
        //如果购物车中没有该物品，则添加到购物车中
        if(shoppingCarts == null || shoppingCarts.size() == 0){
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setUserId(BaseContext.getCurrentId());



            //添加到购物车中
            if(shoppingCartDTO.getDishId() != null){
                Dish dish = dishMapper.selectById(shoppingCartDTO.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());


            }else {
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());

            }

            shoppingCartMapper.addCart(shoppingCart);
        }else{
            ShoppingCart shoppingCart = shoppingCarts.get(0);
            shoppingCart.setNumber(shoppingCarts.get(0).getNumber() + 1);
            //数量加1
            shoppingCartMapper.updateNumberById(shoppingCart);

        }

    }

    @Override
    public List<ShoppingCart> list() {
        return shoppingCartMapper.list(BaseContext.getCurrentId());
    }

    @Override
    public void clean() {

        shoppingCartMapper.deleteById(BaseContext.getCurrentId());
    }
}
