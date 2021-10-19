package com.fishedee.jpa_boost.sample;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fishedee.jpa_boost.lint.JPALintIgnore;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Getter
public class SalesOrder {
    @Embeddable
    @ToString
    @Getter
    public static class Item{
        private String name;

        private Long count;

        protected Item(){

        }

        public Item(String name,Long count){
            this.name = name;
            this.count = count;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String customName;

    private String address;

    @JPALintIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    //@Fetch(FetchMode.SELECT)
    @OrderColumn
    private List<Item> items = new ArrayList<>();

    @Transient
    @JsonIgnore
    @Autowired
    private UserRepository userRepository;

    @Transient
    @JsonIgnore
    private UserRepository userRepository2;

    @Transient
    private UserRepository userRepository3;

    @Transient
    private List<UserRepository> userRepository4;

    protected SalesOrder(){

    }

    public SalesOrder(String customName,String address){
        this.customName = customName;
        this.address = address;
    }

    public void addItem(String name, Long count){
        this.items.add(new Item(name,count));
    }
}
