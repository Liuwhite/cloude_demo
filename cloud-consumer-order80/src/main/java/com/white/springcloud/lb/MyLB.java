package com.white.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MyLB implements LoadBalancer{

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public final int gerAndIncrement() {
        int current;
        int next;

        do {
            current = atomicInteger.get();
            next = current >= 2147483647 ? 0 : current + 1;
            //当前值和期望值一致就修改，否则跳出循环，打印当前值
        } while (!this.atomicInteger.compareAndSet(current, next));
        System.out.println("***第几次访问，次数next：" + next);
        return next;
    }

    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {
        int index = gerAndIncrement() % serviceInstances.size();
        return serviceInstances.get(index);
    }
}
