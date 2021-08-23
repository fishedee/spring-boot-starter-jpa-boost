package com.fishedee.jpa_boost.curd;

import com.fishedee.jpa_boost.JPABoostException;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

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
            String[] orderInfo =single.split(" ");
            if( orderInfo.length == 1 ){
                orderDirection = "asc";
                orderColumn = orderInfo[0];
            }else if( orderInfo.length == 2){
                orderDirection = orderInfo[1];
                orderColumn =orderInfo[0];
            }else{
                throw new JPABoostException(1,"不合法的排序输入:"+sort,null);
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
