package com.fishedee.jpa_boost.lint;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MethodWrapper {

    private Method method;
    private Class clazz;
    private Assert assertUtil;

    public MethodWrapper(Method method, Class clazz){
        this.method = method;
        this.clazz = clazz;
        this.assertUtil = new Assert(this.clazz+"("+this.method.getName()+")");
    }

    public boolean isToStringName(){
        return method.getName().equals("toString");
    }

    public void checkToStringName(){
        this.assertUtil.assertTrue("shouldHaveToStringName",isToStringName());

        this.assertUtil.assertTrue("shouldPublicAccess",Modifier.isPublic(method.getModifiers()));
    }
}
