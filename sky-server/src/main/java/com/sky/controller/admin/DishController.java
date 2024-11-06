package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@Slf4j
@RestController
@Api(tags = "菜品管理")
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping()
    @ApiOperation("新增菜品)")
    public Result saveWithFlavor(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品");
        dishService.saveWithFlavor(dishDTO);
        cleanCache("dish_" + dishDTO.getCategoryId().toString());
        log.info("新增菜品只把对应的分类缓存删除了");
        return Result.success();


    }


    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result page( DishPageQueryDTO dishPageQueryDTO) {
        PageResult pageResult =  dishService.page( dishPageQueryDTO);

        return Result.success(pageResult);
    }

    @ApiOperation("删除菜品")
    @DeleteMapping()
    public Result delete(String ids) {

        String[] idArray = ids.split(",");
        Integer[] idsArray = new Integer[idArray.length];
        for (int i = 0; i < idArray.length; i++) {
            idsArray[i] = Integer.parseInt(idArray[i]);
        }
        dishService.removeByIds(idsArray);

        // 删除菜品 把redis中的所有的dish缓存也全都进行删除
        cleanCache("dish_*");

        return Result.success();

    }
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id) {
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    @PutMapping()
    public Result update(@RequestBody DishDTO dishDTO) {
        dishService.updateWithFlavor(dishDTO);
        cleanCache("dish_*");
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("起售或停售")
    public Result updateStatus(@PathVariable Integer status, Long id) {
        log.info("status:{}", status);
        dishService.updateStatus(status, id);
        cleanCache("dish_*");
        return Result.success();
    }

    //根据分类id查询菜品
    @GetMapping("/list")
    public Result<List<Dish>> list(Long categoryId) {
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    private void cleanCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);

    }



}
