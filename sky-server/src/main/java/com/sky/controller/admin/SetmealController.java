package com.sky.controller.admin;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.Cacheable;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/setmeal")
public class SetmealController {


    @Autowired
    private SetmealService setmealService;



    @PostMapping()
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("setmealDTO:{}", setmealDTO);
        setmealService.save(setmealDTO);
        return Result.success();

    }

    @GetMapping("page")
    public Result page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("setmealPageQueryDTO:{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);

    }

    @DeleteMapping()
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result delete(@RequestParam List<Long> ids) {
        log.info("ids:{}", ids);

        setmealService.delete(ids);
        return Result.success();
    }

    /**
     *根据id查询套餐信息
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id){
        SetmealVO setmealVO=setmealService.getSetmealWithDishById(id);
        return Result.success(setmealVO);
    }

    @PutMapping()
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("setmealDTO:{}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();

    }

    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result updateStatus(@PathVariable Integer status, @RequestParam Long id) {
        log.info("status:{},ids:{}", status, id);
        setmealService.updateStatus(status, id);
        return Result.success();

    }



}
