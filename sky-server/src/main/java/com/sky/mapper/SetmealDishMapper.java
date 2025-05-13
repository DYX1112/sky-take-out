package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: 杜宇翔
 * @CreateTime: 2025-05-13
 * @Description: 套餐菜品
 */
@Mapper
public interface SetmealDishMapper {

    List<Long> getSeatmealIdByDishId(List<Long> ids);


}
