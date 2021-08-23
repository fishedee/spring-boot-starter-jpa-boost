package com.fishedee.jpa_boost.curd;

import lombok.ToString;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fish on 2021/4/27.
 */
public interface CurdFilter {
    Predicate[] filter(CriteriaQuery query,CriteriaBuilder cb, From root);

    boolean shouldDistinct();
}
