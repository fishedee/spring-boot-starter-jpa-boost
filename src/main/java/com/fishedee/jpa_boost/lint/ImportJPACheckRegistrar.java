package com.fishedee.jpa_boost.lint;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.*;


@Slf4j
public class ImportJPACheckRegistrar implements
        ImportBeanDefinitionRegistrar, ResourceLoaderAware , BeanFactoryAware {

    private ResourceLoader resourceLoader;

    private BeanFactory beanFactory;

    private Logger  logger = LoggerFactory.getLogger(getClass());

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory var1) throws BeansException{
        this.beanFactory = var1;
    }

    private List<String> getBasePackageList(AnnotationMetadata importingClassMetadata,AnnotationAttributes annotationAttributes){
        String[] basePackages = annotationAttributes.getStringArray("basePackages");
        List<String> basePackageList = new ArrayList<String>(Arrays.asList(basePackages));

        if( basePackageList.size() == 0){
            //当没有输入包名的时候,就用注解所在类的包
            try {
                //以下这句用的是入口类的所在的包
                //basePackageList = AutoConfigurationPackages.get(this.beanFactory);
                String annotationClass = importingClassMetadata.getClassName();
                String importPackage = Class.forName(annotationClass).getPackage().getName();
                basePackageList.add(importPackage);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        log.info("EnableJPACheck basePackages {}",basePackageList);
        return basePackageList;
    }
    private ProcessorConfig getConfig(AnnotationAttributes annotationAttributes){
        //获取配置
        Class superEntityClass = annotationAttributes.getClass("superEntityClass");
        Class superEmbeddableClass = annotationAttributes.getClass("superEmbeddableClass");
        Class[] extraLinters = annotationAttributes.getClassArray("extraLinters");
        boolean allowIdHaveGeneratedValue = annotationAttributes.getBoolean("allowIdHaveGeneratedValue");

        ProcessorConfig config = new ProcessorConfig();
        config.setAllowIdHaveGeneratedValue(allowIdHaveGeneratedValue);
        config.setSuperEmbeddableClass(superEntityClass);
        config.setSuperEmbeddableClass(superEmbeddableClass);
        config.setExtraLinters(extraLinters);
        return config;
    }
    @Override
    public void registerBeanDefinitions(
            AnnotationMetadata importingClassMetadata,
            BeanDefinitionRegistry registry) {
        Long beginTime = System.currentTimeMillis();
        AnnotationAttributes annotationAttributes = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(
                        EnableJPALint.class.getName(), false));

        List<String> basePackageList = getBasePackageList(importingClassMetadata,annotationAttributes);
        ProcessorConfig config = getConfig(annotationAttributes);

        Processor.setConfig(this.beanFactory,config);

        //开始扫描包并添加到工厂
        JPAClassScanner scanner = new JPAClassScanner(registry);
        scanner.setResourceLoader(resourceLoader);
        scanner.addIncludeFilter(new AssignableTypeFilter(Object.class));
        scanner.scan(basePackageList.toArray(new String[]{}));

        Long endTime = System.currentTimeMillis();
        log.info("@EnableJPALint time : {} ms",endTime-beginTime);
    }
}
