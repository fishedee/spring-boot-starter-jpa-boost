package com.fishedee.jpa_boost.curd;

import com.fishedee.jpa_boost.JPABoostException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Created by fish on 2021/4/16.
 */
public class CurdRepository<T,U extends Serializable> {

    @Autowired
    private QueryRepository queryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Class itemClass;
    private String entityName;
    private String displayEntityName;

    public CurdRepository(String displayEntityName){
        this.displayEntityName = displayEntityName;
    }

    public CurdRepository(){
        this.displayEntityName = "";
    }

    @PostConstruct
    public void init(){
        ParameterizedType ptype = (ParameterizedType) this.getClass().getGenericSuperclass();
        itemClass = (Class<T>) ptype.getActualTypeArguments()[0];
        Metamodel metadata = entityManager.getMetamodel();
        entityName = metadata.entity(itemClass).getName();

        if( this.displayEntityName.isEmpty() ){
            this.displayEntityName = entityName+"实体";
        }
    }

    protected List<T> getAll(boolean shouldReadOnly,boolean shouldLock){
        Query query = entityManager.createQuery("select i from "+entityName+" i");
        if( shouldReadOnly ){
            query = query.setHint(org.hibernate.jpa.QueryHints.HINT_READONLY,true);
        }
        if( shouldLock ){
            query = query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        }
        return (List<T>)query.getResultList();
    }

    public List<T> getAll(){
        return this.getAll(false,false);
    }

    public List<T> getAllForRead(){
        return this.getAll(true,false);
    }

    public List<T> getAllForLock(){
        return this.getAll(false,true);
    }

    protected T get(U id,boolean shouldReadOnly,boolean shouldLock){
        Query query = entityManager.createQuery("select i from "+entityName+" i where i.id = :id")
                .setParameter("id",id);
        if( shouldReadOnly ){
            query = query.setHint(org.hibernate.jpa.QueryHints.HINT_READONLY,true);
        }
        if( shouldLock ){
            query = query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        }
        List<T> result = (List<T>)query.getResultList();
        if( result.size() != 0 ){
            return result.get(0);
        }else{
            throw new JPABoostException(1,"找不到"+id+"的"+displayEntityName,null);
        }
    }

    public T get(U id){
        if( id == null ){
            throw new JPABoostException(1,"传入的"+displayEntityName+"id为null",null);
        }
        T result = (T)entityManager.find(itemClass,id);
        if( result == null ){
            throw new JPABoostException(1,"找不到"+id+"的"+displayEntityName,null);
        }
        return result;
    }

    public T getForRead(U id){
        if( id == null ){
            throw new JPABoostException(1,"传入的"+displayEntityName+"id为null",null);
        }
        return this.get(id,true,false);
    }

    public T getForLock(U id){
        if( id == null ){
            throw new JPABoostException(1,"传入的"+displayEntityName+"id为null",null);
        }

        T result =  (T)entityManager.find(itemClass,id,LockModeType.PESSIMISTIC_WRITE);

        if( result == null ){
            throw new JPABoostException(1,"找不到"+id+"的"+displayEntityName,null);
        }
        return result;
    }

    protected List<T> getBatch(Collection<U> ids,boolean shouldReadOnly,boolean shouldLock){
        Query query = entityManager.createQuery("select i from "+entityName+" i where i.id in (:ids)")
                .setParameter("ids",ids);
        if( shouldReadOnly ){
            query = query.setHint(org.hibernate.jpa.QueryHints.HINT_READONLY,true);
        }
        if( shouldLock ){
            query = query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        }
        return (List<T>)query.getResultList();
    }

    public List<T> getBatch(Collection<U> id){
        return this.getBatch(id,false,false);
    }

    public List<T> getBatchForReady(Collection<U> id){
        return this.getBatch(id,true,false);
    }

    public List<T> getBatchForLock(Collection<U> id){
        return this.getBatch(id,false,true);
    }

    public void add(T country){
        if(entityManager.contains(country) == true){
            throw new RuntimeException("对象已经被持久化了"+country);
        }
        entityManager.persist(country);
    }

    public void del(T country){
        if( entityManager.contains(country) == false){
            throw new RuntimeException("未持久化的对象不能被删除"+country);
        }
        entityManager.remove(country);
    }

    public void clearAll(){
        Metamodel metadata = entityManager.getMetamodel();
        EntityType t = metadata.entity(itemClass);
        entityManager.createQuery("delete from "+t.getName()).executeUpdate();
    }

    protected Page<List<T>> findByFilter(CurdFilterable filerable,Pageable pageable,boolean shouldReadOnly,boolean shouldLock){
        return this.queryRepository.findByFilter(itemClass,filerable,pageable,shouldReadOnly,shouldLock);
    }
}
