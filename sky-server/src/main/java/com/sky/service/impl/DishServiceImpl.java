package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Autowired
    private SetmealMapper setmealMapper;


    @Transactional
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        BeanUtils.copyProperties(dishDTO, dish);

        //首先要插入dish 这样才能返回dish的id
        dishMapper.dishInsert(dish);
        Long id = dish.getId();


        if(flavors != null && !flavors.isEmpty()){
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dish.getId());
            }
            dishFlavorMapper.insertBatch(flavors);
        }
        //批量插入dish_flavor

    }

    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {

        PageHelper.startPage( dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.getByPage(dishPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());

    }


    @Transactional
    @Override
    public void removeByIds(Integer[] idsArray) {
        //如果该菜品是启用的状态不能进行删除
        for(Integer id : idsArray){
            Dish dish = dishMapper.selectById((long) id);
            if(dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //该菜品被包含在套餐中不能进行删除
        Integer nums = setMealDishMapper.queryByDishIds(idsArray);
        if(nums > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //该菜品对应的口味也需要全进行删除
        for (Integer id : idsArray) {
            dishFlavorMapper.deleteByDishId((long) id);
        }
        //删除对应的菜品
        for (Integer id : idsArray) {
            Integer count = dishMapper.countByDishId((long) id);
            if(count == 0){
                throw new RuntimeException("没有该菜品，不能删除");
            }
            dishMapper.deleteById((long) id);
        }
    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        Dish dish = dishMapper.selectById(id);

        //获取flavors
        List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }


    @Transactional
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        //原来的flavor都删除掉
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        List<DishFlavor> flavors = dishDTO.getFlavors();

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.updateById(dish);

        if(flavors != null && !flavors.isEmpty()){
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dish.getId());
            }
        }
        //批量插入新的flavor
        dishFlavorMapper.insertBatch(flavors);
        //更新dish



    }

    @Override
    public void updateStatus(Integer status, Long id) {
        Dish dish = Dish.builder().id(id).status(status).build();
        log.info("dish:{}", dish);
        log.info("开始更新");
        dishMapper.update(dish);
    }

    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;

    }

    @Override
    public List<Dish> list(Long categoryId) {
        log.info("categoryId:{}", categoryId);
        Dish build = Dish.builder().categoryId(categoryId).status(StatusConstant.ENABLE).build();
        List<Dish> list = dishMapper.list(build);
        return list;
    }
}
