package com.fishedee.jpa_boost;

import org.w3c.dom.Attr;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.metamodel.SingularAttribute;

/**
 * Created by fish on 2021/4/28.
 */
public class CurdFilterColumnExpression implements CurdFilterExpression{

    public static class Attribute{
        public SingularAttribute attribute;

        public String name;

        public Attribute(SingularAttribute attribute){
            this.attribute = attribute;
        }

        public Attribute(String name){
            this.name = name;
        }
    }
    private Attribute attribute;

    private CurdFilterRoot root;

    private CurdFilterColumnExpression expression;

    public CurdFilterColumnExpression(CurdFilterRoot root,Attribute attribute){
        this.root = root;

        this.attribute = attribute;
    }

    public CurdFilterColumnExpression(CurdFilterColumnExpression expression,Attribute attribute){
        this.expression = expression;

        this.attribute = attribute;
    }

    @Override
    public Expression getExpression(CriteriaQuery query,CriteriaBuilder cb, From root){
        if( this.root != null){
            From newRoot = this.root.getRoot(query,cb,root);
            if( this.attribute.attribute != null ){
                return newRoot.get(this.attribute.attribute);
            }else{
                return newRoot.get(this.attribute.name);
            }
        }else{
            Path expression = (Path)this.expression.getExpression(query,cb,root);
            if( this.attribute.attribute != null ){
                return expression.get(this.attribute.attribute);
            }else{
                return expression.get(this.attribute.name);
            }
        }
    }

    public CurdFilterColumnExpression get(String name){
        return new CurdFilterColumnExpression(this,new Attribute(name));
    }

    public CurdFilterColumnExpression get(SingularAttribute attribute){
        return new CurdFilterColumnExpression(this,new Attribute(attribute));
    }
}
