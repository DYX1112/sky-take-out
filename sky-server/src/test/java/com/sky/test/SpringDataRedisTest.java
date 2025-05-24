package com.sky.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 杜宇翔
 * @CreateTime: 2025-05-14
 * @Description: ceshi
 */
//@SpringBootTest
public class SpringDataRedisTest {

    //@Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisTemplate(){
        System.out.println(redisTemplate);

        ValueOperations valueOperations = redisTemplate.opsForValue();


    }

    @Test
    public void testString(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("city","四川");
        String city = (String) valueOperations.get("city");

        valueOperations.set("yzm","312435",60, TimeUnit.SECONDS);

        valueOperations.setIfAbsent("lock","1");

        valueOperations.setIfAbsent("lock","2");
    }

    @Test
    public void testHash(){
        redisTemplate.opsForHash().put("people","username","dyx");
        redisTemplate.opsForHash().put("people","password","123456");
        redisTemplate.opsForHash().put("people","sex","man");

        redisTemplate.opsForHash().get("people","username");
        redisTemplate.opsForHash().get("people","password");

        redisTemplate.opsForHash().keys("people");
        redisTemplate.opsForHash().values("people");

        redisTemplate.opsForHash().delete("people","sex");
    }

    @Test
    public void testList(){
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.leftPushAll("student","1","2","3");
        listOperations.rightPushAll("student","4","5","6");

        listOperations.range("student",0,19);

        listOperations.leftPop("student");
        listOperations.rightPop("student");

        Long size = listOperations.size("student");
        System.out.println(size);
    }

    @Test
    public void testSet(){
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add("mySet","dyx","dfy");
        setOperations.add("mySet1","dyx","lll");

        setOperations.members("mySet");
        setOperations.size("mySet");

        setOperations.intersect("mySet","mySet1");
        setOperations.union("mySet","mySet1");

        setOperations.remove("mySet1","lll");
    }

    @Test
    public void testZSet(){
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("zset1","dyx",90.00);
        zSetOperations.add("zset1","dfy",80.00);
        zSetOperations.add("zset1","lll",99.00);

        zSetOperations.range("zset1",0,-1);

        zSetOperations.incrementScore("zset1","dfy",9);
        zSetOperations.remove("zset1","lll");
    }


    @Test
    public void testCommon(){
        Set keys = redisTemplate.keys("*");
        System.out.println(keys);

        Boolean dyx = redisTemplate.hasKey("dyx");
        System.out.println(dyx);

        System.out.println(redisTemplate.type("dyx"));

        Boolean mySet1 = redisTemplate.delete("mySet1");
    }
}
