package com.fishedee.jpa_boost.sample;

import com.fishedee.jpa_boost.lint.JPALinter;
import com.fishedee.jpa_boost.lint.JPAWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyJPALinter implements JPALinter {
    @Override
    public void process(JPAWrapper bean){
        log.info("lint class {} isEntity: {}",bean.getClassName(),bean.hasEntityAnnotation());
    }
}
