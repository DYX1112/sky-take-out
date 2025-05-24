package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: 杜宇翔
 * @CreateTime: 2025-05-18
 * @Description: 订单详情
 */
@Mapper
public interface OrderDetailMapper {

    void insertBatch(List<OrderDetail> lists);

    /**
     * 根据订单id查询订单明细
     * @param orderId
     * @return
     */
    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> getByOrderId(Long orderId);

    List<GoodsSalesDTO> getTop10(LocalDateTime begin, LocalDateTime end);
}
