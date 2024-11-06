package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 套餐业务实现
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetMealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }

    @Override
    public void save(SetmealDTO setmealDTO) {
        //判断名字是否重复
        String name = setmealDTO.getName();
        Setmeal setmeal = setmealMapper.getByName(name);
        if(setmeal != null) {
            throw new RuntimeException("套餐名称已存在");
        }
        Setmeal setmeal1 = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal1);
        //保存套餐 回显套餐id
        setmealMapper.save(setmeal1);
        //保存套餐和菜品关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for(SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmeal1.getId());
        }
        setmealDishMapper.insertBatch(setmealDishes);

    }

    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.page(setmealPageQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;


    }

    @Transactional
    @Override
    public void delete(List<Long> ids) {
        for(Long id : ids) {
            //判断是否被订单使用
            Setmeal setmeal = setmealMapper.getById(id);
            if(setmeal.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }


        }
        //删除套餐
        for(Long id : ids) {
            setmealMapper.deleteById(id);
            setmealDishMapper.deleteBySetmealId(id);
        }
        //删除套餐和菜品关系

    }

    @Override
    public SetmealVO getSetmealWithDishById(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    @Transactional

    @Override
    public void update(SetmealDTO setmealDTO) {
        //更新套餐
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);
        //更新套餐和菜品关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        Long setmealId = setmealDTO.getId();
        setmealDishMapper.deleteBySetmealId(setmealId);
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealId);
        }
        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Transactional
    @Override
    public void updateStatus(Integer status, Long id) {
        if(status == StatusConstant.DISABLE) {
            setmealMapper.update( Setmeal.builder().id(id).status(status).build() );
        }else {
            List<Dish> dishes = setmealMapper.getDishesBySetmealId(id);
            for(Dish dish : dishes) {
                if(dish.getStatus() == StatusConstant.DISABLE) {
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
            setmealMapper.update( Setmeal.builder().id(id).status(status).build() );
        }

    }


}
