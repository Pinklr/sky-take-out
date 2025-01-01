package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;


    @Scheduled(cron = "0 * * * * ?")
    // 每分钟执行一次
    public void processOverTimeOrder() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime time = localDateTime.plusMinutes(-15);

        List<Orders> orders = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT, time);
        for (Orders order : orders) {
            order.setStatus(Orders.CANCELLED);
            order.setCancelReason("订单超时自动取消");
            order.setCancelTime(LocalDateTime.now());
            orderMapper.update(order);
        }

    }

    //一直处于派送中的任务
    // 每天凌晨1点执行一次
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime time = localDateTime.plusMinutes(-60);
        List<Orders> orders = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, localDateTime);
        for (Orders order : orders) {
            order.setStatus(Orders.COMPLETED);
            orderMapper.update(order);
        }
    }


}
