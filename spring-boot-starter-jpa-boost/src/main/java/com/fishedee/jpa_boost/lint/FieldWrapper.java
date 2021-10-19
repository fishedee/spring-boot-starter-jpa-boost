package com.fishedee.jpa_boost.lint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Slf4j
public class FieldWrapper {

    private Field field;
    private Class clazz;
    private Assert assertUtil;

    public FieldWrapper(Field field ,Class clazz){
        this.field = field;
        this.clazz = clazz;
        this.assertUtil = new Assert(clazz.getName()+"."+field.getName()+"::[type = "+this.getType().getSimpleName()+"]");
    }

    public boolean isPrivateAccess(){
        return Modifier.isPrivate(this.field.getModifiers());
    }

    public boolean shouldIgnoreLint(){
        JPALintIgnore jpaLintIgnore = this.field.getAnnotation(JPALintIgnore.class);
        return jpaLintIgnore != null;
    }
    public void shouldNotSynthetic(){
        new Assert(this.clazz.getName()).assertFalse(" should be static access ",field.isSynthetic());
    }

    public void shouldNotHaveGeneratedValue(){
        this.assertUtil.assertNull("shouldNotHaveGeneratedValue",this.field.getAnnotation(GeneratedValue.class));
    }

    public void shouldPrivateAccess(){
        this.assertUtil.assertTrue("shouldPrivateAccess",isPrivateAccess());
    }

    public boolean isIdName(){
        return "id".equals(this.getName());
    }

    public boolean isListType(){
        return List.class.equals(this.getType());
    }

    public Class getListElemType(){
        ParameterizedType type = (ParameterizedType)this.field.getGenericType();
        try{
            return Class.forName(type.getActualTypeArguments()[0].getTypeName());
        }catch(ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    public boolean isSetType(){
        return Set.class.equals(this.getType());
    }

    public Class getSetElemType(){
        ParameterizedType type = (ParameterizedType)this.field.getGenericType();
        try{
            return Class.forName(type.getActualTypeArguments()[0].getTypeName());
        }catch(ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    public boolean isMapType(){
        return Map.class.equals(this.getType());
    }

    public Class[] getMapElemType(){
        ParameterizedType type = (ParameterizedType)this.field.getGenericType();
        try{
            Class clazz1 = Class.forName(type.getActualTypeArguments()[0].getTypeName());
            Class clazz2 = Class.forName(type.getActualTypeArguments()[1].getTypeName());
            return new Class[]{clazz1,clazz2};
        }catch(ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    public boolean isEnumType(){
        return this.getType().isEnum();
    }

    public boolean isSatifyDbType(){
        return TypeUtil.isSatifyDbType(this.getType());
    }

    public void shouldSatifyDbType(){
        this.assertUtil.assertTrue("shouldSatifyDbType ",isSatifyDbType());
    }

    public String getName(){
        return this.field.getName();
    }

    public Class getType(){
        return this.field.getType();
    }

    public boolean hasIdAnnotation(){
        Id id = this.field.getAnnotation(Id.class);
        return id != null;
    }

    public void shouldHaveIdAnnotation(){
        assertUtil.assertTrue("shouldHaveIdAnnotation",hasIdAnnotation());
    }

    public boolean hasTransitAnnotation(){
        Transient annotation = this.field.getAnnotation(Transient.class);
        return annotation != null;
    }

    public void shouldHaveTransitAnnotation(){
        assertUtil.assertTrue("shouldHaveTransitAnnotation",hasTransitAnnotation());
    }

    public boolean hasAutowiredAnnotation(){
        Autowired annotation = this.field.getAnnotation(Autowired.class);
        return annotation != null;
    }

    public void shouldHaveAutowiredAnnotation(){
        assertUtil.assertTrue("shouldHaveAutowiredAnnotation",hasAutowiredAnnotation());
    }

    public boolean hasJsonUnwrappedAnnotation(){
        JsonUnwrapped annotation = this.field.getAnnotation(JsonUnwrapped.class);
        return annotation!= null;
    }

    public boolean hasJsonIgnoreAnnotation(){
        JsonIgnore annotation = this.field.getAnnotation(JsonIgnore.class);
        return annotation != null;
    }

    public void shouldHaveJsonIgnoreAnnotation(){
        assertUtil.assertTrue("shouldHaveJsonIgnoreAnnotation",hasJsonIgnoreAnnotation());
    }

    public boolean hasOneToManyAnnotation(){
        OneToMany annotation = this.field.getAnnotation(OneToMany.class);
        return annotation != null;
    }

    public void checkOneToManyAnnotation(){
        assertUtil.assertTrue("shouldHaveOneToManyAnnotation",hasOneToManyAnnotation());

        OneToMany annotation = this.field.getAnnotation(OneToMany.class);
        //检查FetchType
        FetchType fetchType = annotation.fetch();
        this.assertUtil.assertEqual(
                "@OneToMany FetchType",
                fetchType,
                FetchType.EAGER);

        //检查CascadeType
        CascadeType[] cascadeTypes = annotation.cascade();
        this.assertUtil.assertArraySize(
                "@OneToMany CascadeType ",
                cascadeTypes,
                1
        );
        this.assertUtil.assertInArray(
                "@OneToMany CascadeType",
                cascadeTypes[0],
                Arrays.asList(CascadeType.ALL,CascadeType.MERGE)
        );

        //检查orphanRemoval
        if(cascadeTypes[0] == CascadeType.ALL){
            boolean orphanlRemoval = annotation.orphanRemoval();
            this.assertUtil.assertTrue(
                    "@OneToMany shouldHaveOrphanlRemoval",
                    orphanlRemoval
            );
        }
    }

    public boolean hasJoinColumnAnnotation(){
        JoinColumn annotation = this.field.getAnnotation(JoinColumn.class);
        return annotation != null;
    }

    public void checkJoinColumnAnnotation(){
        this.assertUtil.assertTrue("shouldHaveJoinColumnAnnotation",hasJoinColumnAnnotation());

        JoinColumn annotation = this.field.getAnnotation(JoinColumn.class);
        boolean isNullable = annotation.nullable();
        this.assertUtil.assertFalse("@JoinColumn should set nullable is false",false);
    }

    public boolean hasColumnAnnotation(){
        Column annotation = this.field.getAnnotation(Column.class);
        return annotation != null;
    }

    public void shouldHaveColumnAnnotation(){
        this.assertUtil.assertTrue("shouldHaveColumnAnnotation",hasColumnAnnotation());
    }

    public boolean hasMapKeyColumnAnnotation(){
        MapKeyColumn annotation = this.field.getAnnotation(MapKeyColumn.class);
        return annotation != null;
    }

    public void shouldHaveMapKeyColumnAnnotation(){
        this.assertUtil.assertTrue("shouldHaveMapKeyColumnAnnotation",hasMapKeyColumnAnnotation());
    }

    public boolean hasFetchAnnotation(){
        Fetch annotation = this.field.getAnnotation(Fetch.class);
        return annotation != null;
    }

    public void checkFetchAnnotation(){
        assertUtil.assertTrue("shouldHaveFetchAnnotation",hasFetchAnnotation());

        Fetch annotation = this.field.getAnnotation(Fetch.class);
        FetchMode fetchMode = annotation.value();

        this.assertUtil.assertEqual(
                "@Fetch value",
                fetchMode,
                FetchMode.SELECT
        );
    }

    public boolean hasOrderColumnAnnotation(){
        OrderColumn annotation = this.field.getAnnotation(OrderColumn.class);
        return annotation != null;
    }

    public void shouldHaveOrderColumnAnnotation(){
        assertUtil.assertTrue("shouldHaveOrderColumnAnnotation",hasOrderColumnAnnotation());
    }

    public boolean hasEnumeratedAnnotation(){
        Enumerated annotation = this.field.getAnnotation(Enumerated.class);
        return annotation != null;
    }

    public void checkEnumTypeAnnotation(){
        this.assertUtil.assertTrue("hasEnumeratedAnnotation",hasEnumeratedAnnotation());
        Enumerated annotation = this.field.getAnnotation(Enumerated.class);
        EnumType type = annotation.value();
        this.assertUtil.assertEqual("@Type type should be my_enum",type,EnumType.STRING);
    }

    public boolean hasElementCollectionAnnotation(){
        ElementCollection annotation = this.field.getAnnotation(ElementCollection.class);
        return annotation != null;
    }

    public void checkElementCollectionAnnotation(){
        this.assertUtil.assertTrue("shouldHaveElementCollectionAnnotation",hasElementCollectionAnnotation());
        ElementCollection annotation = this.field.getAnnotation(ElementCollection.class);
        //检查FetchType
        FetchType fetchType = annotation.fetch();
        this.assertUtil.assertEqual(
                "@ElementCollection FetchType",
                fetchType,
                FetchType.EAGER);
    }
}
