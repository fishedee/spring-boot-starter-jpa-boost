package com.fishedee.jpa_boost;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fish on 2021/4/28.
 */
public class QueryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private <X> Page<List<X>> findDataByFilter(Class<X> clazz, CurdFilter filter, Pageable pageable, boolean shouldReadOnly, boolean shouldLock){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<X> criteria = cb.createQuery(clazz);
        Root<X> item = criteria.from(clazz);
        criteria = criteria.select(item);
        if( filter.shouldDistinct()) {
            criteria.distinct(true);
        }
        Predicate[] predicates = filter.filter(criteria,cb,item);
        if( predicates.length != 0){
            criteria = criteria.where(predicates);
        }
        if( pageable.shouldSort() ){
            List<Order> orders = new ArrayList<>();
            List<Pageable.SortInfo> sortInfos =  pageable.getSort();
            for(Pageable.SortInfo single :sortInfos ){
                if( single.getDirection() == Pageable.SortDirection.ASC ){
                    orders.add(cb.asc(item.get(single.getColumn())));
                }else{
                    orders.add(cb.desc(item.get(single.getColumn())));
                }
            }
            criteria.orderBy(orders);
        }
        TypedQuery<X> query = entityManager.createQuery(criteria);
        if( pageable.shouldPage() ){
            query.setFirstResult(pageable.getPageIndex());
            query.setMaxResults(pageable.getPageSize());
        }
        if( shouldReadOnly ){
            query = query.setHint(org.hibernate.jpa.QueryHints.HINT_READONLY,true);
        }
        if( shouldLock ){
            query = query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        }
        List<X> data = (List<X>)query.getResultList();
        Page<List<X>> result =  new Page<List<X>>(data);
        if( pageable.shouldPage() ){
            //???????????????????????????
            result.setOffset(pageable.getPageIndex(),pageable.getPageSize());
            if( pageable.getPageIndex() > 0 && pageable.getPageSize() > data.size() &&
                data.size() > 0 ){
                //?????????
                //???????????????????????????????????????????????????????????????????????????????????????????????????????????????count??????
                result.setCount(pageable.getPageIndex()+data.size());
            }
            if( pageable.getPageIndex() == 0 && pageable.getPageSize() > data.size()){
                //??????
                result.setCount(data.size());
            }
        }else{
            //?????????????????????,????????????
            result.setCount(data.size());
        }
        return result;
    }
    private <X> void countDataByFilter(Page<List<X>> result,Class<X> clazz,CurdFilter filter,Pageable pageable){
        if( result.hasCount() ){
            //?????????????????????
            return;
        }
        if( pageable.shouldCountAll() == false ){
            //?????????????????????
            return;
        }
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<X> item = criteria.from(clazz);
        criteria = criteria.select(cb.count(item));
        if( filter.shouldDistinct()) {
            criteria.distinct(true);
        }
        Predicate[] predicates = filter.filter(criteria,cb,item);
        if( predicates.length != 0){
            criteria = criteria.where(predicates);
        }
        Long count = entityManager.createQuery(criteria).getSingleResult();
        result.setCount(count.intValue());
    }
    <X> Page<List<X>> findByFilter(Class<X> clazz,CurdFilterable filerable,Pageable pageable,boolean shouldReadOnly,boolean shouldLock){
        //??????????????????
        CurdFilter filer = null;
        if( filerable == null ){
            filer = new CurdFilterBuilder();
        }else{
            filer = filerable.getFilter();
        }
        if( pageable == null ){
            pageable = new CurdPageAll();
        }

        Page<List<X>> result = this.findDataByFilter(clazz,filer,pageable,shouldReadOnly,shouldLock);

        this.countDataByFilter(result,clazz,filer,pageable);

        return result;
    }

    public <X> Page<List<X>> findByFilter(Class<X> clazz,CurdFilterable filterable,Pageable pageable){
        return this.findByFilter(clazz,filterable,pageable,true,false);
    }
}
