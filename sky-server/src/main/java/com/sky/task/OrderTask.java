package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: 杜宇翔
 * @CreateTime: 2025-05-19
 * @Description: 订单定时任务类
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;
    //每隔一分钟触发一次
    @Scheduled(cron = "0 */1 * * * *")
    public void processTimeOutOrder(){
        log.info("每隔一分钟触发一次");

        LocalDateTime timeout = LocalDateTime.now().plusMinutes(-15);

        List<Orders> orders =  orderMapper.getTimeoutOrderByStatusAndTime(Orders.PENDING_PAYMENT,timeout);

        for (Orders order : orders) {
            order.setCancelTime(LocalDateTime.now());
            order.setCancelReason("订单超时未支付");
            order.setStatus(Orders.UN_PAID);
            orderMapper.update(order);
        }

    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrderByStatusAndTime(){

        log.info("每到凌晨一点触发一次");

        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);

        List<Orders> orders = orderMapper.getDeliveryOrderByStatusAndTime(Orders.DELIVERY_IN_PROGRESS,time);

        for (Orders order : orders) {
            order.setStatus(Orders.COMPLETED);
            orderMapper.update(order);
        }
    }

}
