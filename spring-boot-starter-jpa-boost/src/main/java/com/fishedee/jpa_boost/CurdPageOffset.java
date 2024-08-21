package com.fishedee.jpa_boost;

import lombok.ToString;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fish on 2021/4/27.
 */
@ToString
public class CurdPageOffset implements Pageable {
    private int pageIndex;

    private int pageSize;

    private List<SortInfo>  sort;

    private boolean shouldCountAll;

    public CurdPageOffset(int pageIndex,int pageSize){
        this.setPageIndex(pageIndex);
        this.setPageSize(pageSize);
        this.shouldCountAll = false;
        this.sort = new ArrayList<SortInfo>();
    }

    private void setPageIndex(int pageIndex){
        if( pageIndex < 0 ){
            throw new JPABoostException(1,"分页偏移量不能为负数",null);
        }
        this.pageIndex = pageIndex;
    }

    private void setPageSize(int pageSize){
        if( pageSize <= 0 ){
            throw new JPABoostException(1,"分页数量必须为正数",null);
        }
        this.pageSize = pageSize;
    }

    @Override
    public int getPageIndex(){
        return this.pageIndex;
    }

    @Override
    public int getPageSize(){
        return this.pageSize;
    }

    @Override
    public boolean shouldPage(){
        return true;
    }

    public CurdPageOffset withCount(){
        this.shouldCountAll = true;
        return this;
    }

    @Override
    public boolean shouldCountAll(){
        return this.shouldCountAll;
    }

    public CurdPageOffset withSort(String sort){
        String[] orderList = sort.split(",");
        for( String single : orderList ){
            single = single.trim();
            if( single.isEmpty() ){
                continue;
            }
            String orderDirection = "asc";
            String orderColumn  = "";
            List<String> orderInfo = Arrays.stream(single.split(" ")).map(single2->{
                return single2.trim();
            }).filter(single2->{
                return single2.isEmpty() == false;
            }).collect(Collectors.toList());
            if( orderInfo.size() == 1 ){
                orderDirection = "asc";
                orderColumn = orderInfo.get(0);
            }else if( orderInfo.size() == 2){
                orderDirection = orderInfo.get(1);
                orderColumn = orderInfo.get(0);
            }else{
                orderDirection = orderInfo.get(orderInfo.size() - 1);
                orderInfo.remove(orderInfo.size() - 1);
                orderColumn = Strings.join(orderInfo,' ');
            }

            //添加排序
            this.sort.add(new SortInfo(
                    SortDirection.valueFromString(orderDirection),
                    orderColumn
            ));
        }
        return this;
    }

    @Override
    public boolean shouldSort(){
        return this.sort != null && this.sort.size() != 0;
    }

    @Override
    public List<SortInfo> getSort(){
        return this.sort;
    }
}
