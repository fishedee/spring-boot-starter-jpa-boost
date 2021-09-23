package com.fishedee.jpa_boost;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.metamodel.SingularAttribute;

public class CurdFilterBuilderRoot implements CurdFilterRoot{

    public CurdFilterBuilderRoot(){
    }

    @Override
    public From getRoot(CriteriaQuery query, CriteriaBuilder cb, From root){
        return root;
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
