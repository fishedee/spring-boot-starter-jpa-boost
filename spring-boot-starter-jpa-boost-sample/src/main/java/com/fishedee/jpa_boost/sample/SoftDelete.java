package com.fishedee.jpa_boost.sample;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Embeddable;

@Embeddable
@ToString
@Getter
public class SoftDelete {
    private byte isDelete = 0;

    public SoftDelete(){

    }

    public void delete(){
        if( isDelete == 1 ){
            throw new RuntimeException("已经被停用，不能重复停用");
        }
        this.isDelete = 1;
    }

    public void recover(){
        if( isDelete == 0 ){
            throw new RuntimeException("正常状态，不需要恢复");
        }
        this.isDelete = 0;
    }
}
