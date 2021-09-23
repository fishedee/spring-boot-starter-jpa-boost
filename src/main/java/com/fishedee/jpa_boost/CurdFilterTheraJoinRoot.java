package com.fishedee.jpa_boost;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fish on 2021/4/28.
 */
//TheraJoin只支持inner join的方式
@Slf4j
public class CurdFilterTheraJoinRoot implements CurdFilterRoot{
    private Class clazz;

    private Map<From,From> jonResult;

    private String column1;

    private String column2;

    private CurdFilterBuilder builder;

    public CurdFilterTheraJoinRoot(String column1,Class clazz,String column2,CurdFilterBuilder builder){
        this.clazz = clazz;
        this.column1 = column1;
        this.column2 = column2;
        this.builder = builder;
        this.jonResult = new HashMap<>();
    }

    public CurdFilterTheraJoinRoot(Class clazz){
        this.clazz = clazz;
        this.jonResult = new HashMap<>();
    }

    private From getRootInner(CriteriaQuery query,CriteriaBuilder cb, From root){
        From newTable =  query.from(clazz);
        if( this.builder != null ){
            //加入连接条件
            this.builder.addPrePredicate(cb.equal(root.get(column1),newTable.get(column2)));
        }
        return newTable;
    }

    public From getRoot(CriteriaQuery query,CriteriaBuilder cb, From root){
        From result =  jonResult.get(root);
        if( result == null ){
            result = getRootInner(query,cb,root);
            jonResult.put(root,result);
        }
        return result;
    }

    @Override
    public CurdFilterColumnExpression get(String name){
        return new CurdFilterColumnExpression(this,new CurdFilterColumnExpression.Attribute(name));
    }

    @Override
    public CurdFilterColumnExpression get(SingularAttribute attribute){
        return new CurdFilterColumnExpression(this,new CurdFilterColumnExpression.Attribute(attribute));
    }}
