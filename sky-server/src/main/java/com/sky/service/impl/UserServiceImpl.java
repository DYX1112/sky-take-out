package com.sky.service.impl;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 杜宇翔
 * @CreateTime: 2025-05-15
 * @Description: 用户服务层
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String wechatAddr = "https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    private  WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;
    public UserServiceImpl(WeChatProperties weChatProperties) {
        this.weChatProperties = weChatProperties;
    }

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User login(UserLoginDTO userLoginDTO) {

        String openId = getOpenId(userLoginDTO.getCode());
        // 判断openid是否为空，为空抛出业务异常
         if(openId == null){
             throw new LoginFailedException("用户登录失败");
         }
        // 判断当前用户是否为新用户
        User user = userMapper.getUserByOpenId(openId);
        // 如果为新用户，自动注册
        if(user == null){
            user = User.builder()
                .openid(openId)
                .createTime(LocalDateTime.now())
                .build();
            userMapper.insert(user);
        }
        // 返回这个user对象
        return user;
    }

    private String getOpenId(String code){
        //调用微信接口服务，获取openid
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid",weChatProperties.getAppid());
        paramMap.put("secret",weChatProperties.getSecret());
        paramMap.put("js_code",code);
        paramMap.put("grant_type","authorization_code");
        String s = HttpClientUtil.doGet(wechatAddr, paramMap);
        JSONObject jo = (JSONObject)JSON.parse(s);
        String openid = jo.getString("openid");
        return openid;
    }
}
