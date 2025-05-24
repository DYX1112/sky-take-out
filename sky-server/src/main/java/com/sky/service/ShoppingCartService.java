package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * @Author: 杜宇翔
 * @CreateTime: 2025-05-16
 * @Description: 购物车
 */
public interface ShoppingCartService {

    /**
     * 购物车增加商品
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查询购物车商品
     * @return
     */
    List<ShoppingCart> list();

    /**
     * 清空购物车
     */
    void delete();

    /**
     * 减去购物车中某个商品数量
     */
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
