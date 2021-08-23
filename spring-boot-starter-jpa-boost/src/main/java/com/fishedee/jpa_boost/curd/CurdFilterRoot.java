package com.fishedee.jpa_boost.curd;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * Created by fish on 2021/4/28.
 */
public interface CurdFilterRoot {
    From getRoot(CriteriaQuery query, CriteriaBuilder cb, From root);

    CurdFilterExpression get(String name);
}
