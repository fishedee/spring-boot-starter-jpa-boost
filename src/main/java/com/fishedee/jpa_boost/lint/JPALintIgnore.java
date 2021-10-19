package com.fishedee.jpa_boost.lint;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JPALintIgnore {
}
