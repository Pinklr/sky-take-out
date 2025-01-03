package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        log.info("categoryId:{}", categoryId);
        // 如果redis中存在数据就直接返回 没有则查询数据库并且存入缓存
//        if (redisTemplate.hasKey("dish_" + categoryId)) {
//            List<DishVO> dishVOS = (List<DishVO>) redisTemplate.opsForValue().get("dish_" + categoryId);
//            log.info("从缓存中获取数据list:{}", dishVOS);
//            return Result.success(dishVOS);
//        }
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品

        List<DishVO> list = dishService.listWithFlavor(dish);
        // 将查询到的数据存入redis
//        redisTemplate.opsForValue().set("dish_" + categoryId, list);
        log.info("数据存入缓存list:{}", list);
        return Result.success(list);
    }

}
