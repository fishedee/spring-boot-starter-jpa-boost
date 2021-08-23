package com.fishedee.jpa_boost.sample;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@ToString
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Long age;

    protected User(){

    }

    public User(String name,Long age){
        this.name = name;
        this.age = age;
    }

    public void setName(String name){
        this.name = name;
    }
}
