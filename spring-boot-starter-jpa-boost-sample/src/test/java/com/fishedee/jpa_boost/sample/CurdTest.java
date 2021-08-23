package com.fishedee.jpa_boost.sample;

import com.fishedee.jpa_boost.CurdRepository;
import com.fishedee.jpa_boost.JsonAssertUtil;
import com.fishedee.jpa_boost.QueryRepository;
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
public class CurdTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testGetAll(){
        List<User> userList = userRepository.getAll();
        assertEquals(userList.size(),0);
    }

    //修改操作的，需要有@Transicational注解
    @Test
    @Transactional
    public void testCurd(){
        //添加
        User user = new User("fish",123L);
        User user2 = new User("cat",456L);
        userRepository.add(user);
        userRepository.add(user2);

        List<User> userList = userRepository.getAll();
        assertEquals(userList.size(),2);

        //查询
        List<User> iNameList = userRepository.getByName("i");
        JsonAssertUtil.checkEqualStrict("[{id:1,name:\"fish\",age:123}]",iNameList);

        //删除
        userRepository.del(user);
        List<User> userList2 = userRepository.getAll();
        assertEquals(userList2.size(),1);
        assertEquals(userList2.get(0).getName(),"cat");

        //修改
        user2.setName("mk");
        List<User> userList3 = userRepository.getAll();
        assertEquals(userList3.size(),1);
        assertEquals(userList3.get(0).getName(),"mk");
    }
}
