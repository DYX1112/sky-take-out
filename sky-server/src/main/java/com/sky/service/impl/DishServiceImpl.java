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
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;

/**
 * @Author: 杜宇翔
 * @CreateTime: 2025-05-13
 * @Description: 菜品相关
 */
@Service

public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * 新增菜品
     * @param dishDTO
     */
    @Transactional
    @Override
    public void saveWithFlavors(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        dishMapper.saveWithFlavors(dish);

        Long dishId = dish.getId();

        //向口味表中插入n条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null && !flavors.isEmpty()){
            flavors.forEach(flavor->{
                flavor.setDishId(dishId);
            });
            dishFlavorMapper.saveFlavors(flavors);
        }

    }

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {

        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> dishes = dishMapper.pageQuery(dishPageQueryDTO);

        return new PageResult(dishes.getTotal(),dishes.getResult());
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Transactional
    @Override
    public void delDish(List<Long> ids) {

        //判断菜品是否在起售中
        for (Long id : ids) {
            Dish dish = dishMapper.getDishById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //判断菜品是否存在于套餐中
        List<Long> setmeals = setmealDishMapper.getSeatmealIdByDishId(ids);
        if(setmeals!=null && !setmeals.isEmpty()){
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }

//        // 删除菜品表中的菜品数据
//        for (Long id : ids) {
//            dishMapper.delDish(id);
//            //删除菜品关联的口味数据
//            dishFlavorMapper.delDish(id);
//        }

        //删除菜品表中的菜品数据
        dishMapper.delDishes(ids);

        //批量删除菜品关联的口味数据
        dishFlavorMapper.delDishes(ids);
    }

    /**
     * 根据id查询菜品
     * @param dishId
     * @return
     */
    @Override
    public DishVO getDishWithFlavorById(Long dishId) {
        Dish dish = dishMapper.getDishById(dishId);

        List<DishFlavor> dishFlavors = dishFlavorMapper.getDishFlavorByDishId(dishId);

        DishVO dishVO = new DishVO();

        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;

    }

    /**
     * 修改菜品
     * @param dishDTO
     */
    @Override
    public void updateDish(DishDTO dishDTO) {
        //修改基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.updateDish(dish);

        //先删除原先口味在插入现有口味
        dishFlavorMapper.delDish(dishDTO.getId());
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null && !flavors.isEmpty()){
            flavors.forEach(flavor->{
                flavor.setDishId(dishDTO.getId());
            });
            dishFlavorMapper.saveFlavors(flavors);
        }
    }
}
