package com.fishedee.jpa_boost;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.criteria.JoinType;
import javax.persistence.metamodel.SingularAttribute;

/**
 * Created by fish on 2021/4/28.
 */
@Slf4j
public class CurdFilterCombineJoinRoot implements CurdFilterRoot{
    private String column;

    private JoinType joinType;

    private Map<From,From> jonResult;

    public CurdFilterCombineJoinRoot(String column, JoinType joinType){
        this.column = column;
        this.joinType = joinType;
        this.jonResult = new HashMap<>();
    }

    public From getRoot(CriteriaQuery query,CriteriaBuilder cb, From root){
        From result =  jonResult.get(root);
        if( result == null ){
            result = root.join(this.column, this.joinType);
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
    }
}
