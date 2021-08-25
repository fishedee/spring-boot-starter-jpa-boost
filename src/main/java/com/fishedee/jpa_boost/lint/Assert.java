package com.fishedee.jpa_boost.lint;

import java.util.List;

public class Assert {
    private String prefix;

    public Assert(String prefix){
        this.prefix = prefix;
    }

    public<T> void assertNotNull(String msg,T target){
        if( target == null){
            throw new Error(this.prefix+"::"+msg);
        }
    }

    public<T> void assertNull(String msg,T target){
        if( target != null){
            throw new Error(this.prefix+"::"+msg);
        }
    }


    public void assertTrue(String msg,boolean target){
        if( target == false){
            throw new Error(this.prefix+"::"+msg);
        }
    }

    public void assertFalse(String msg,boolean target){
        if( target == true){
            throw new Error(this.prefix+"::"+msg);
        }
    }


    public <T> void assertEqual(String msg,T left, T right){
        if( left.equals(right) == false ){
            throw new Error(this.prefix+"::"+msg+" -> "+left+" != "+right);
        }
    }

    public <T> void assertArraySize(String msg,T[] data,int size){
        if( data == null || data.length != size){
            throw new Error(this.prefix+"::"+msg+" -> size != "+size);
        }
    }

    public <T> void assertInArray(String msg,T left, List<T> right){
        //found
        for( T singleRight : right ){
            if( singleRight.equals(left)){
                return;
            }
        }
        throw new Error(this.prefix+"::"+msg+" -> "+left+" not in ( "+right+")");
    }
}
