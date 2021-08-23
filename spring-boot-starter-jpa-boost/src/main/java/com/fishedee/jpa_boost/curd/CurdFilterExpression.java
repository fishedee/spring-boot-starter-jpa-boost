package com.fishedee.jpa_boost.curd;

import javax.persistence.criteria.*;

/**
 * Created by fish on 2021/4/28.
 */
public interface CurdFilterExpression {
    Expression getExpression(CriteriaQuery query, CriteriaBuilder cb, From root);
}
