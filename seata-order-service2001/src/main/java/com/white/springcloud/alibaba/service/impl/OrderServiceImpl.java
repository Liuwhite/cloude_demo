package com.white.springcloud.alibaba.service.impl;

import com.white.springcloud.alibaba.dao.OrderDao;
import com.white.springcloud.alibaba.domain.Order;
import com.white.springcloud.alibaba.service.AccountService;
import com.white.springcloud.alibaba.service.OrderService;
import com.white.springcloud.alibaba.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;
    @Resource
    private StorageService storageService;
    @Resource
    private AccountService accountService;


    @Override
    @GlobalTransactional(name = "fsp-create-order",rollbackFor = Exception.class)
    public void create(Order order) {
        log.info("--->新建订单");
        orderDao.create(order);

        log.info("--->订单微服务调用库存，做扣减,count");
        storageService.decrease(order.getProductId(), order.getCount());
        log.info("--->订单微服务调用库存，做扣减,end");

        log.info("--->订单微服务调用账户，做扣减,money");
        accountService.decrease(order.getUserId(), order.getMoney());
        log.info("--->订单微服务调用账户，做扣减,end");

        //修改订单的状态 0 -1
        log.info("--->修改订单的状态开始，0->");
        orderDao.update(order.getUserId(),0);
        log.info("--->修改订单的状态结束，0->");

        log.info("--->下订单结束了，！！！");

    }
}
