package com.fishedee.jpa_boost;

public class JPABoostException extends RuntimeException{
    private int code;

    public JPABoostException(int code, String message,Object data){
        super(message);
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }
}
