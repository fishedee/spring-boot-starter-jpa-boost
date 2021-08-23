package com.fishedee.jpa_boost.sample;

import com.fishedee.jpa_boost.CurdFilterBuilder;
import com.fishedee.jpa_boost.CurdPageAll;
import com.fishedee.jpa_boost.CurdRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepository extends CurdRepository<User,Long> {
    public UserRepository(){
        super("用户");
    }

    public List<User> getByName(String name){
        //使用CurdFilterBuilder来做任意查询
        CurdFilterBuilder builder = new CurdFilterBuilder();
        builder.like("name","%"+name+"%");
        return this.findByFilter(builder, new CurdPageAll(),false,false).getData();
    }
}
