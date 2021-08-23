package com.fishedee.jpa_boost.curd;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;

/**
 * Created by fish on 2021/4/28.
 */
public class CurdFilterColumnExpression implements CurdFilterExpression{
    private String column;

    private CurdFilterRoot root;

    public CurdFilterColumnExpression(CurdFilterRoot root,String column){
        this.root = root;

        this.column = column;
    }

    @Override
    public Expression getExpression(CriteriaQuery query,CriteriaBuilder cb, From root){
        From newRoot = this.root.getRoot(query,cb,root);
        return newRoot.get(this.column);
    }
}
