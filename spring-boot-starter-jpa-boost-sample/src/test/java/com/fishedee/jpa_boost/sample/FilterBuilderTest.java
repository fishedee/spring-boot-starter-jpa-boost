package com.fishedee.jpa_boost.sample;

import com.fishedee.jpa_boost.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.metamodel.model.domain.internal.SingularAttributeImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.metamodel.SingularAttribute;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
public class FilterBuilderTest {

    @Autowired
    private  ContactRepository contactRepository;

    @Autowired
    private QueryRepository queryRepository;

    private Map<Long,Contact> contactMap;

    @BeforeEach
    public void setUp(){
        List<Contact> contactList = contactRepository.getAll();

        contactMap = contactList.stream().collect(Collectors.toMap(Contact::getId,(e)->e));
    }

    @Test
    @Sql("classpath:/init.sql")
    public void like(){
        CurdFilterBuilder builder = new CurdFilterBuilder();
        CurdFilterRoot root = builder.root();
        builder.like("name","%客户%");

        List<Contact> contactList = queryRepository.findByFilter(Contact.class,builder,new CurdPageAll()).getData();

        List<Contact> target = Arrays.asList(this.contactMap.get(1001L),this.contactMap.get(1003L));
        assertIterableEquals(target,contactList);
    }

    @Test
    @Sql("classpath:/init.sql")
    public void equal(){
        CurdFilterBuilder builder = new CurdFilterBuilder();
        builder.equal("contactCategoryName","桂林");
        List<Contact> contactList = queryRepository.findByFilter(Contact.class,builder,new CurdPageAll()).getData();

        List<Contact> target = Arrays.asList(this.contactMap.get(1004L));
        assertIterableEquals(target,contactList);
    }


    @Test
    @Sql("classpath:/init.sql")
    public void notEqual(){
        CurdFilterBuilder builder = new CurdFilterBuilder();
        builder.notEqual("contactCategoryName","广州");
        List<Contact> contactList = queryRepository.findByFilter(Contact.class,builder,new CurdPageAll()).getData();

        List<Contact> target = Arrays.asList(this.contactMap.get(1001L),this.contactMap.get(1003L),this.contactMap.get(1004L));
        assertIterableEquals(target,contactList);
    }

    @Test
    @Sql("classpath:/init.sql")
    public void equalNested(){
        CurdFilterBuilder builder = new CurdFilterBuilder();
        builder.equal(builder.root().get(Contact_.softDelete).get("isDelete"),1);
        List<Contact> contactList = queryRepository.findByFilter(Contact.class,builder,new CurdPageAll()).getData();

        List<Contact> target = Arrays.asList(this.contactMap.get(1002L));
        assertIterableEquals(target,contactList);
    }

    @Test
    @Sql("classpath:/init.sql")
    public void equalNested2(){
        CurdFilterBuilder builder = new CurdFilterBuilder();
        builder.equal(builder.root().get(Contact_.softDelete).get("isDelete"),0);
        List<Contact> contactList = queryRepository.findByFilter(Contact.class,builder,new CurdPageAll()).getData();

        List<Contact> target = Arrays.asList(this.contactMap.get(1001L),this.contactMap.get(1003L),this.contactMap.get(1004L));
        assertIterableEquals(target,contactList);
    }

    @Test
    @Sql("classpath:/init.sql")
    public void in(){
        CurdFilterBuilder builder = new CurdFilterBuilder();
        builder.in("contactCategoryName",Arrays.asList("桂林","广州"));
        List<Contact> contactList = queryRepository.findByFilter(Contact.class,builder,new CurdPageAll()).getData();

        List<Contact> target = Arrays.asList(this.contactMap.get(1002L),this.contactMap.get(1004L));
        assertIterableEquals(target,contactList);
    }

    @Test
    @Sql("classpath:/init.sql")
    public void or(){
        CurdFilterBuilder builder = new CurdFilterBuilder();
        builder.or(
                new CurdFilterBuilder()
                        .equal("isCustomer",0)
                        .equal("isSuppiler",0)
        );
        List<Contact> contactList = queryRepository.findByFilter(Contact.class,builder,new CurdPageAll()).getData();

        List<Contact> target = Arrays.asList(this.contactMap.get(1001L),this.contactMap.get(1002L),this.contactMap.get(1003L));
        assertIterableEquals(target,contactList);
    }

    @Test
    @Sql("classpath:/init.sql")
    public void and(){
        CurdFilterBuilder builder = new CurdFilterBuilder();
        builder.equal("contactCategoryName","桂林");
        builder.equal("isCustomer",1);
        List<Contact> contactList = queryRepository.findByFilter(Contact.class,builder,new CurdPageAll()).getData();

        List<Contact> target = Arrays.asList(this.contactMap.get(1004L));
        assertIterableEquals(target,contactList);
    }

    @Test
    @Sql("classpath:/init.sql")
    public void combineLikeAndEqual(){
        CurdFilterBuilder builder = new CurdFilterBuilder();
        builder.equal("contactCategoryName","梧州");
        builder.like("name","%客户%");
        List<Contact> contactList = queryRepository.findByFilter(Contact.class,builder,new CurdPageAll()).getData();

        List<Contact> target = Arrays.asList(this.contactMap.get(1003L));
        assertIterableEquals(target,contactList);
    }

    @Test
    @Sql("classpath:/init.sql")
    public void innerJoinLocal(){
        CurdFilterBuilder builder = new CurdFilterBuilder();
        CurdFilterRoot phoneRoot = builder.innerJoin("phones");
        builder.equal(phoneRoot.get("name"),"C_dog");
        List<Contact> contactList = queryRepository.findByFilter(Contact.class,builder,new CurdPageAll()).getData();

        List<Contact> target = Arrays.asList(this.contactMap.get(1003L));
        assertIterableEquals(target,contactList);
    }

    @Test
    @Sql("classpath:/init.sql")
    public void leftJoinLocal_currentField(){
        CurdFilterBuilder builder = new CurdFilterBuilder();
        CurdFilterRoot phoneRoot = builder.leftJoin("phones");
        builder.or(
            new CurdFilterBuilder()
                    .equal("name","A客户")
                    .equal(phoneRoot.get("name"),"C_dog")
        );
        List<Contact> contactList = queryRepository.findByFilter(Contact.class,builder,new CurdPageAll()).getData();

        List<Contact> target = Arrays.asList(this.contactMap.get(1001L),this.contactMap.get(1003L));
        assertIterableEquals(target,contactList);
    }

    @Test
    @Sql("classpath:/init.sql")
    public void innerJoinLocal_currentField(){
        CurdFilterBuilder builder = new CurdFilterBuilder();
        CurdFilterRoot phoneRoot = builder.innerJoin("phones");
        builder.or(
                new CurdFilterBuilder()
                        .equal("name","A客户")
                        .equal(phoneRoot.get("name"),"C_dog")
        );
        List<Contact> contactList = queryRepository.findByFilter(Contact.class,builder,new CurdPageAll()).getData();

        List<Contact> target = Arrays.asList(this.contactMap.get(1003L));
        assertIterableEquals(target,contactList);
    }

    @Test
    @Sql("classpath:/init.sql")
    public void getBatch(){
        List<Contact> contactList = contactRepository.getBatch(Arrays.asList(1001L,1003L,1001L));

        List<Contact> target = Arrays.asList(this.contactMap.get(1001L),this.contactMap.get(1003L));
        assertIterableEquals(target,contactList);
    }
}
