package com.fishedee.jpa_boost.autoconfig;

import com.fishedee.jpa_boost.QueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;

@Slf4j
@Configuration
@EnableConfigurationProperties(JPABoostProperties.class)
public class JPABoostAutoConfiguration {
    private final AbstractApplicationContext applicationContext;

    private final JPABoostProperties properties;

    public JPABoostAutoConfiguration(AbstractApplicationContext applicationContext, JPABoostProperties properties) {
        this.applicationContext = applicationContext;
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean(QueryRepository.class)
    @ConditionalOnProperty(value = "spring.jpa-boost.enable", havingValue = "true")
    public QueryRepository queryRepository(){
        return new QueryRepository();
    }
}
