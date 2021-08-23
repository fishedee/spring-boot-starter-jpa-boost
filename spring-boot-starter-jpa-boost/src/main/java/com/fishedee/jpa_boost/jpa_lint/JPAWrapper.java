package com.fishedee.jpa_boost.jpa_lint;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class JPAWrapper {

    private Class clazz;

    private Assert assertUtil;

    private JPAWrapper(Class clazz){
        this.clazz = clazz;
        assertUtil = new Assert(clazz.getName());
    }

    public static Map<Class,JPAWrapper>  cache = new HashMap<>();

    public static JPAWrapper getInstance(Class clazz){
        JPAWrapper result = cache.get(clazz);
        if( result == null ){
            result = new JPAWrapper(clazz);
            cache.put(clazz,result);
        }
        return result;
    }

    public String getClassName(){
        return this.clazz.getName();
    }

    public void checkPublicAccess(){
        this.assertUtil.assertTrue("should be public access",Modifier.isPublic(this.clazz.getModifiers()));
    }

    public boolean hasEntityAnnotation(){
        Entity entity = (Entity)this.clazz.getAnnotation(Entity.class);
        return entity != null;
    }

    public void shouldHaveEntityAnnotation(){
        this.assertUtil.assertTrue("shouldHaveEntityAnnotation",hasEntityAnnotation());
    }

    public boolean hasEmbeddableAnnotation(){
        Embeddable embeddable = (Embeddable)this.clazz.getAnnotation(Embeddable.class);
        return embeddable != null;
    }

    public void shouldHaveEmbeddableAnnotation(){
        this.assertUtil.assertTrue("shouldHaveEmbeddableAnnotation",hasEmbeddableAnnotation());
    }

    public boolean isDirectExtendClass(Class clazz){
        return this.clazz.getSuperclass()== clazz;
    }

    public List<ConstructorWrapper> getConstructor(){
        List<ConstructorWrapper> result = new ArrayList<>();
        Constructor[] constructors = this.clazz.getDeclaredConstructors();
        for( int i = 0 ;i != constructors.length ;i++){
            result.add(new ConstructorWrapper(constructors[i],clazz));
        }
        return result;
    }

    public Map<String,MethodWrapper> getMethods(){
        Method[] methods = this.clazz.getDeclaredMethods();
        Map<String,MethodWrapper> result = new HashMap<>();
        for( int i = 0 ;i != methods.length ;i++){
            result.put(
                    methods[i].getName(),
                    new MethodWrapper(methods[i],clazz)
            );
        }
        return result;
    }

    public Map<String,FieldWrapper> getFields(){
        Field[] fields = this.clazz.getDeclaredFields();
        Map<String,FieldWrapper> result = new HashMap<>();
        for( int i = 0 ;i != fields.length ;i++){
            if(Modifier.isStatic(fields[i].getModifiers())){
                //static成员略过
                continue;
            }
            result.put(
                    fields[i].getName(),
                    new FieldWrapper(fields[i],clazz)
            );
        }
        return result;
    }

    public boolean isSatifyDbType(){
        return TypeUtil.isSatifyDbType(this.clazz);
    }

    public void shouldSatifyDbType(){
        this.assertUtil.assertTrue("shouldSatifyDbType ",isSatifyDbType());
    }
}
