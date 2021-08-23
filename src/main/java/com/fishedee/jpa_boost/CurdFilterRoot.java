package com.fishedee.jpa_boost;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;

/**
 * Created by fish on 2021/4/28.
 */
public interface CurdFilterRoot {
    From getRoot(CriteriaQuery query, CriteriaBuilder cb, From root);

    CurdFilterExpression get(String name);
}
