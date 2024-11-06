package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);
    

    @AutoFill(OperationType.INSERT)
    void dishInsert(Dish dish);


    Page<DishVO> getByPage(DishPageQueryDTO dishPageQueryDTO);

    @Select("select count(*) from dish where id=#{id}")
    Integer countByDishId(long id);


    @Delete("delete from dish where id=#{id}")
    void deleteById(long id);


    @Select("Select * from dish where id=#{id}")
    Dish selectById(long id);

    @AutoFill(OperationType.UPDATE)
    @Update("update dish set name=#{name},category_id=#{categoryId}," +
            "price=#{price},image=#{image},description=#{description},status=#{status} where id=#{id}")

    void updateById(Dish dish);

    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);


    List<Dish> list(Dish dish);


}
