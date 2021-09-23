package com.fishedee.jpa_boost;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fish on 2021/4/27.
 */
public class CurdFilterBuilder implements CurdFilter,CurdFilterable{

    private interface CurdAndFilterCallback{
        Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root);
    }

    private List<CurdAndFilterCallback> predicates;

    private List<Predicate> prePredicates;

    private boolean distinct;

    public CurdFilterBuilder(){
        this.predicates = new ArrayList<CurdAndFilterCallback>();
        this.prePredicates = new ArrayList<>();
        this.distinct = false;
    }

    public void distinct(){
        this.distinct = true;
    }

    public boolean shouldDistinct(){
        return this.distinct;
    }

    public CurdFilterBuilder equal(String name, Object arg){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.equal(root.get(name),arg);
            }
        });
        return this;
    }

    //只有当前包的其他类可以用
    void addPrePredicate(Predicate single){
        this.prePredicates.add(single);
    }

    public CurdFilterBuilder equal(CurdFilterExpression expression, Object arg){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.equal(expression.getExpression(query,cb,root),arg);
            }
        });
        return this;
    }

    public <Y extends Comparable<? super Y>> CurdFilterBuilder lessThan(String name, Y arg){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.lessThan(root.get(name),arg);
            }
        });
        return this;
    }

    public <Y extends Comparable<? super Y>> CurdFilterBuilder lessThan(CurdFilterExpression expression, Y arg){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.lessThan(expression.getExpression(query,cb,root),arg);
            }
        });
        return this;
    }

    public <Y extends Comparable<? super Y>> CurdFilterBuilder lessThanOrEqualTo(String name, Y arg){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.lessThanOrEqualTo(root.get(name),arg);
            }
        });
        return this;
    }

    public <Y extends Comparable<? super Y>> CurdFilterBuilder lessThanOrEqualTo(CurdFilterExpression expression, Y arg){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.lessThanOrEqualTo(expression.getExpression(query,cb,root),arg);
            }
        });
        return this;
    }

    public <Y extends Comparable<? super Y>> CurdFilterBuilder greaterThan(String name, Y arg){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.greaterThan(root.get(name),arg);
            }
        });
        return this;
    }

    public <Y extends Comparable<? super Y>> CurdFilterBuilder greaterThan(CurdFilterExpression expression, Y arg){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.greaterThan(expression.getExpression(query,cb,root),arg);
            }
        });
        return this;
    }

    public <Y extends Comparable<? super Y>> CurdFilterBuilder greaterThanOrEqualTo(String name, Y arg){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.greaterThanOrEqualTo(root.get(name),arg);
            }
        });
        return this;
    }

    public <Y extends Comparable<? super Y>> CurdFilterBuilder greaterThanOrEqualTo(CurdFilterExpression expression, Y arg){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.greaterThanOrEqualTo(expression.getExpression(query,cb,root),arg);
            }
        });
        return this;
    }

    public CurdFilterBuilder in(String name, Object arg){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.in(root.get(name)).value(arg);
            }
        });
        return this;
    }

    public CurdFilterBuilder in(CurdFilterExpression expression, Object arg){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.in(expression.getExpression(query,cb,root)).value(arg);
            }
        });
        return this;
    }


    public CurdFilterBuilder like(String name, String arg){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.like(root.get(name),arg);
            }
        });
        return this;
    }

    public CurdFilterBuilder like(CurdFilterExpression expression, String arg){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.like(expression.getExpression(query,cb,root),arg);
            }
        });
        return this;
    }

    public CurdFilterBuilder contain(String name,Object arg){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.isMember(arg,root.get(name));
            }
        });
        return this;
    }

    public CurdFilterBuilder contain(CurdFilterExpression expression,Object arg){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.isMember(arg,expression.getExpression(query,cb,root));
            }
        });
        return this;
    }

    public CurdFilterBuilder isNull(String name){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.isNull(root.get(name));
            }
        });
        return this;
    }

    public CurdFilterBuilder isNull(CurdFilterExpression expression){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.isNull(expression.getExpression(query,cb,root));
            }
        });
        return this;
    }

    public CurdFilterBuilder isNotNull(String name){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.isNotNull(root.get(name));
            }
        });
        return this;
    }

    public CurdFilterBuilder isNotNull(CurdFilterExpression expression){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.isNotNull(expression.getExpression(query,cb,root));
            }
        });
        return this;
    }

    public CurdFilterBuilder and(CurdFilter filter){
        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.and(filter.filter(query,cb,root));
            }
        });
        return this;
    }
    public CurdFilterBuilder or(CurdFilter filter){


        this.predicates.add(new CurdAndFilterCallback() {
            @Override
            public Predicate filter(CriteriaQuery query,CriteriaBuilder cb, From root) {
                return cb.or(filter.filter(query,cb,root));
            }
        });
        return this;
    }

    public CurdFilterBuilderRoot root(){
        return new CurdFilterBuilderRoot();
    }

    public CurdFilterRoot innerJoin(String name){
        return new CurdFilterCombineJoinRoot(name,JoinType.INNER);
    }

    public CurdFilterRoot leftJoin(String name){
        return new CurdFilterCombineJoinRoot(name,JoinType.LEFT);
    }

    public CurdFilterRoot rightJoin(String name){
        return new CurdFilterCombineJoinRoot(name,JoinType.RIGHT);
    }

    public CurdFilterRoot innerJoin(Class clazz){
        return new CurdFilterTheraJoinRoot(clazz);
    }

    public CurdFilterRoot innerJoin(String column1,Class clazz,String column2){
        return new CurdFilterTheraJoinRoot(column1,clazz,column2,this);
    }

    public Predicate[] filter(CriteriaQuery query,CriteriaBuilder cb, From root){
        //先处理middleWhere,它可能会产生prePredicates
        List<Predicate> middleWhere = new ArrayList<>();
        for( CurdAndFilterCallback callback : this.predicates){
            middleWhere.add(callback.filter(query,cb,root));
        }
        //处理完毕以后,才重新刷一遍predicate
        List<Predicate> allWhere = new ArrayList<>();
        allWhere.addAll(this.prePredicates);
        //使用以后要清空,否则下次使用会用2次
        this.prePredicates.clear();
        allWhere.addAll(middleWhere);
        return (Predicate[]) allWhere.toArray(new Predicate[allWhere.size()]);
    }

    public CurdFilter getFilter(){
        return this;
    }
}
