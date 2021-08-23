package com.fishedee.jpa_boost;

/**
 * Created by fish on 2021/4/28.
 */
public class CurdPageAll extends CurdPageOffset {
    public CurdPageAll(){
        super(0,10);
    }

    @Override
    public int getPageIndex(){
        return 0;
    }

    @Override
    public int getPageSize(){
        return 0;
    }

    @Override
    public boolean shouldPage(){
        return false;
    }

    @Override
    public boolean shouldCountAll(){return false;}
}
