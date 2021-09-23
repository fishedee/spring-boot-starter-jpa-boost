package com.fishedee.jpa_boost.sample;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.metamodel.model.domain.internal.SingularAttributeImpl;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@ToString
@Getter
@Slf4j
public class Contact {
    @Embeddable
    @ToString
    @Getter
    public static class Phone{
        private String name;

        private String phone;

        protected Phone(){

        }

        public Phone(String name,String phone){
            this.name = name;
            this.phone = phone;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private byte isCustomer;

    private byte isSuppiler;

    private Long contactCategoryId;

    private String contactCategoryPath;

    private String contactCategoryName;

    private String remark;

    @JsonUnwrapped
    private SoftDelete softDelete;

    @ElementCollection(fetch=FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @OrderColumn
    private List<Phone> phones = new ArrayList<>();

    protected Contact(){
    }

    public Contact(ContactDTO dto){
        this.softDelete = new SoftDelete();
        this.set(dto);
    }

    public void mod(ContactDTO dto){
        this.set(dto);
    }

    private void set(ContactDTO dto){
        this.name = dto.getName();
        this.isCustomer = dto.getIsCustomer();
        this.isSuppiler = dto.getIsSuppiler();
        this.contactCategoryId = dto.getContactCategoryId();
        this.contactCategoryPath = dto.getContactCategoryPath();
        this.contactCategoryName = dto.getContactCategoryName();
        this.remark = dto.getRemark();
        this.phones = dto.getPhones().stream().map((single)->{
            return new Phone(single.getName(),single.getPhone());
        }).collect(Collectors.toList());
    }

    public void refreshContactCategoryInfo(String name,String path){
        if( name == null || Strings.isBlank(name)){
            throw new RuntimeException("name 不能为空");
        }
        if( path == null || Strings.isBlank(path)){
            throw new RuntimeException("path 不能为空");
        }
        this.contactCategoryName = name;
        this.contactCategoryPath = path;
    }

    public void softDelete(){
        this.softDelete.delete();
    }

    public void softRecover(){
        this.softDelete.recover();
    }
}
