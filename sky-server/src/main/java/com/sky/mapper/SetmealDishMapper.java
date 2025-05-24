package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * @Author: 杜宇翔
 * @CreateTime: 2025-05-13
 * @Description: 套餐菜品
 */
@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id查询套餐id
     * @param ids
     * @return
     */
    List<Long> getSeatmealIdByDishId(List<Long> ids);


    /**
     * 插入套餐
     * @param setmealDishes
     */
    void insertData(List<SetmealDish> setmealDishes);

    /**
     * 查询套餐带菜品
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getDishBySetmealId(Long id);

    /**
     * 删除菜品
     * @param id
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void delDishes(Long id);
}
