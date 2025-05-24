package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 杜宇翔
 * @CreateTime: 2025-05-13
 * @Description: 新增菜品
 */

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增菜品
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "setmealCache",key = "#setmealDTO.categoryId")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐：{}",setmealDTO);
        setmealService.saveWithDishes(setmealDTO);
        return Result.success();
    }

    /**
     * 分页查询菜品
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("page")
    @ApiOperation("分页查询套餐")

    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("分页查询：{}",setmealPageQueryDTO);
        PageResult pageResult =  setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("(批量)删除套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result<String> delSetmeal(@RequestParam List<Long> ids){
        log.info("批量删除套餐：{}",ids);
        setmealService.delSetmeal(ids);
        return Result.success("删除成功");
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据分类id查询套餐")
    public Result<List<SetmealVO>> getSetmealById(@PathVariable Long id){
        log.info("根据id查询:{}",id);
        List<SetmealVO> setmealVO = setmealService.getSetmealWithDishesById(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改菜品
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改菜品：{}",setmealDTO);
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }


    @PostMapping("/status/{status}")
    @ApiOperation("套餐停售开售")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result stopOrStart(@PathVariable Integer status, Long id){
        log.info("套餐目前状态：{}",status);
        setmealService.setStatus(status,id);
        return Result.success();
    }

}
