package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: 杜宇翔
 * @CreateTime: 2025-05-13
 * @Description: 口味表mapper
 */
@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入多条口味
     * @param flavors
     */
    void saveFlavors(List<DishFlavor> flavors);

    /**
     * 删除菜品关联的口味数据
     * @param id
     */
    @Delete("delete from dish_flavor where dish_id = #{id}")
    void delDish(Long id);

    /**
     * 批量删除菜品口味
     * @param ids
     */
    void delDishes(List<Long> ids);

    /**
     * 查询菜品口味
     * @param dishId
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getDishFlavorByDishId(Long dishId);
}
