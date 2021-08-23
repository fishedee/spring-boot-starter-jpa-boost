package com.fishedee.jpa_boost.curd;

import com.fishedee.jpa_boost.JPABoostException;

import java.util.List;

/**
 * Created by fish on 2021/4/28.
 */
public class Page<T> {
    int pageIndex;
    int pageSize;
    int count;
    T data;

    public Page(T data){
        this.count = -1;
        this.pageIndex = -1;
        this.pageSize = -1;
        this.data = data;
    }

    public void setOffset(int pageIndex,int pageSize){
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public void setCount(int count){
        this.count = count;
    }

    public T getData(){
        return this.data;
    }

    public boolean hasCount(){
        return this.count >= 0;
    }

    public int getCount(){
        return this.count;
    }

    public int getPageIndex(){
        return this.pageIndex;
    }

    public int getPageSize(){
        return this.pageSize;
    }
}
