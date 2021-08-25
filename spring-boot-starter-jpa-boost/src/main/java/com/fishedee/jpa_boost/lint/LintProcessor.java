package com.fishedee.jpa_boost.lint;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class LintProcessor {

    public void process(String clazzName){
        try{
            Class clazz = Class.forName(clazzName);
            this.process(clazz);
        }catch(ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    private static Set<Class> cache = new HashSet<>();

    //每个类型仅需要检查一次
    static JPAWrapper process(Class clazz){
        boolean hasProcess = cache.contains(clazz);
        JPAWrapper wrapper = JPAWrapper.getInstance(clazz);
        if( hasProcess ){
            return wrapper;
        }
        cache.add(clazz);

        new Processor(wrapper).process();
        return wrapper;
    }
}
