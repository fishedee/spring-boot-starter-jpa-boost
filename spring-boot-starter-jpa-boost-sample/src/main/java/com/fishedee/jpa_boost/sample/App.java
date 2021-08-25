package com.fishedee.jpa_boost.sample;

import com.fishedee.jpa_boost.lint.EnableJPALint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableJPALint(
        allowIdHaveGeneratedValue = true,
        extraLinters = {MyJPALinter.class}
)
public class App 
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class,args);
    }
}
