package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 杜宇翔
 * @CreateTime: 2025-05-14
 * @Description: 店铺相关设置
 */
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

    private static final String key = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 设置状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置状态")
    public Result setStatus(@PathVariable Integer status){
        log.info("商家店铺状态：{}",status == 1?"营业中":"已打样");
        redisTemplate.opsForValue().set(key,status);
        return Result.success();
    }

    /**
     * 查询店铺状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("查询状态")
    public Result<Integer> getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(key);
        log.info("商家店铺状态：{}",status == 1?"营业中":"已打样");
        return Result.success(status);
    }
}
