package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @Author: 杜宇翔
 * @CreateTime: 2025-05-15
 * @Description: 用户数据表操作
 */

@Mapper
public interface UserMapper {

    @Select("select * from user where openid = #{openId}")
    User getUserByOpenId(String openId);

    void insert(User user);

    @Select("select * from user where id = #{userId}")
    User getById(Long userId);

    /**
     * 查询用户数
     * @param m
     * @return
     */
    Integer countUserByTime(Map<String, Object> m);
}
