package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * @Author: 杜宇翔
 * @CreateTime: 2025-05-13
 * @Description: 菜品相关
 */
public interface DishService {

    /**
     * 保存菜品
     * @param dishDTO
     */
    void saveWithFlavors(DishDTO dishDTO);

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 删除菜品
     * @param id
     */
    void delDish(List<Long> id);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    DishVO getDishWithFlavorById(Long id);

    /**
     * 修改菜品
     * @param dishDTO
     */
    void updateDish(DishDTO dishDTO);

    /**
     * 设置菜品状态
     * @param status
     * @param id
     */
    void setStatus(Integer status, Long id);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    List<DishVO> list(Long categoryId);
}
