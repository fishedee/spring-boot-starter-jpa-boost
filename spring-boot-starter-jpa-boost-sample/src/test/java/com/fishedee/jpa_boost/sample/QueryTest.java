package com.fishedee.jpa_boost.sample;

import com.fishedee.jpa_boost.*;
import com.fishedee.jpa_boost.sample.User;
import com.fishedee.jpa_boost.sample.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class QueryTest {
    @Autowired
    private SalesOrderRepository salesOrderRepository;

    @Autowired
    private QueryRepository queryRepository;

    @Test
    public void testGetAll(){
        List<SalesOrder> salesOrderList = salesOrderRepository.getAll();
        assertEquals(salesOrderList.size(),0);
    }

    //修改操作的，需要有@Transicational注解
    @Test
    @Transactional
    public void testQuery(){
        //添加
        for(int i = 0 ;i !=100;i++){
            SalesOrder salesOrder = new SalesOrder("fish_"+i,"addr_"+i);
            for( int j = 0 ;j != 3;j++){
                salesOrder.addItem("items_"+j,new Long(j));
            }
            salesOrderRepository.add(salesOrder);
        }

        //任意查询，不分页
        CurdFilterBuilder builder = new CurdFilterBuilder();
        builder.equal("customName","fish_0");
        Page<List<SalesOrder>> salesOrderPage = queryRepository.findByFilter(SalesOrder.class,builder,new CurdPageAll());

        JsonAssertUtil.checkEqualNotStrict("{pageIndex:-1,pageSize:-1,count:1}",salesOrderPage);
        JsonAssertUtil.checkEqualNotStrict("[{id:1,customName:\"fish_0\",address:\"addr_0\"}]",salesOrderPage.getData());
        JsonAssertUtil.checkEqualNotStrict("[{name:\"items_0\",count:0},{name:\"items_1\",count:1},{name:\"items_2\",count:2}]",salesOrderPage.getData().get(0).getItems());

        //任意查询，分页
        CurdFilterBuilder builder2 = new CurdFilterBuilder();
        builder2.like("customName","%fish%");
        Page<List<SalesOrder>> salesOrderPage2 = queryRepository.findByFilter(SalesOrder.class,builder2,new CurdPageOffset(5,10).withCount());
        JsonAssertUtil.checkEqualNotStrict("{pageIndex:5,pageSize:10,count:100}",salesOrderPage2);
        JsonAssertUtil.checkEqualNotStrict("{id:6,customName:\"fish_5\",address:\"addr_5\"}",salesOrderPage2.getData().get(0));
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void testQueryCount(){
        //添加
        for(int i = 0 ;i !=100;i++){
            User user = new User("user_"+i,(long)i);
            userRepository.add(user);
        }

        for( int i = 0 ;i != 1000;i++){
            CurdFilterBuilder builder = new CurdFilterBuilder();
            Page<List<User>> users = queryRepository.findByFilter(User.class,builder,new CurdPageOffset(i,10).withCount());
            assertEquals(100,users.getCount());
        }

        for( int i = 1 ;i != 1000;i++){
            CurdFilterBuilder builder = new CurdFilterBuilder();
            Page<List<User>> users = queryRepository.findByFilter(User.class,builder,new CurdPageOffset(0,i).withCount());
            assertEquals(100,users.getCount());
        }
    }

}

