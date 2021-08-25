package com.fishedee.jpa_boost.lint;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

/**
 * Created by fish on 2021/4/14.
 */
@Slf4j
public class JPAClassScanner extends ClassPathBeanDefinitionScanner {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private LintProcessor processor ;
    private ProcessorConfig config;

    public JPAClassScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
        processor = new LintProcessor();
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        processor.process(beanDefinition.getBeanClassName());
        //将所有candiate都设置为false，不是注入，我们只是为了检查
        return false;
    }
}