package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @Author: 杜宇翔
 * @CreateTime: 2025-05-20
 * @Description: 数据统计相关接口
 */
@Mapper
public interface ReportMapper {


    Double turnoverStatistics(Map<String, Object> m);
}
