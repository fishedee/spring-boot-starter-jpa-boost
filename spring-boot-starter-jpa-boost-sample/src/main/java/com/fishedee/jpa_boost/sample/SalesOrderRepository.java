package com.fishedee.jpa_boost.sample;

import com.fishedee.jpa_boost.CurdRepository;
import org.springframework.stereotype.Component;

@Component
public class SalesOrderRepository extends CurdRepository<SalesOrder,Long> {
    public SalesOrderRepository(){
        super("销售订单");
    }
}
