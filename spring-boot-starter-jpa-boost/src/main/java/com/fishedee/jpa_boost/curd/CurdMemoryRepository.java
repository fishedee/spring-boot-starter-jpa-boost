package com.fishedee.jpa_boost.curd;

import com.fishedee.jpa_boost.JPABoostException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fish on 2021/5/3.
 */
//该存储库用来做测试用
public class CurdMemoryRepository<T> {
    private List<T> data = new ArrayList<T>();

    private Class clazz;

    private Field idField;

    public CurdMemoryRepository(){
        ParameterizedType ptype = (ParameterizedType) this.getClass().getGenericSuperclass();
        clazz = (Class<T>) ptype.getActualTypeArguments()[0];
        try{
            this.idField = clazz.getDeclaredField("id");
        }catch(NoSuchFieldException e){
            throw new RuntimeException(e);
        }
        this.idField.setAccessible(true);
    }

    private  Long getId(Object target){
        try{
            return (Long)this.idField.get(target);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public T get(Long id){
        for(T single:this.data){
            Long current = this.getId(single);
            if( current.equals(id)){
                return single;
            }
        }
        throw new JPABoostException(1,"找不到"+id+this.clazz,null);
    }

    public void add(T data){
        Long currentId = this.getId(data);
        if( currentId == null ){
            throw new JPABoostException(1,this.clazz+"缺少id了",null);
        }
        for( T single :this.data ){
            Long id = this.getId(single);
            if( id.equals(currentId)){
                throw new JPABoostException(1,"重复的ID"+currentId,null);
            }
        }
        this.data.add(data);
    }
}
