package com.fishedee.jpa_boost.jpa_lint;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ConstructorWrapper {
    private Constructor constructor;
    private Class clazz;
    private Assert assertUtil;

    public ConstructorWrapper(Constructor constructor, Class clazz){
        this.constructor = constructor;
        this.clazz = clazz;
        this.assertUtil = new Assert(this.clazz+"("+this.constructor.getName()+")");
    }

    public boolean isEmptyParameter(){
        return this.constructor.getParameterCount() == 0;
    }

    public void checkPublicOrProtectedAccess(){
        int modifiers = this.constructor.getModifiers();
        this.assertUtil.assertTrue("should have public or protected access",
                Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers));
    }
}
