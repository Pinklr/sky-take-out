package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {


    public List<ShoppingCart> select(ShoppingCartDTO shoppingCartDTO);




    @Insert("insert into shopping_cart (user_id, dish_id,setmeal_id,image,amount,name,number,create_time, dish_flavor) " +
            "values (#{userId},#{dishId},#{setmealId},#{image},#{amount},#{name},#{number},#{createTime},#{dishFlavor})")
    void addCart(ShoppingCart shoppingCart);


    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    @Select("select * from shopping_cart where user_id=#{currentId}")
    List<ShoppingCart> list(Long currentId);


    @Delete("delete from shopping_cart where user_id = #{currentId}")
    void deleteById(Long currentId);
}
