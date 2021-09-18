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
public class MyGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private byte isEnabled;

    protected MyGroup() {

    }

    public MyGroup(byte isEnabled){
        this.isEnabled = isEnabled;
    }

    public void mod(byte isEnabled){
        this.isEnabled = isEnabled;
    }
}

