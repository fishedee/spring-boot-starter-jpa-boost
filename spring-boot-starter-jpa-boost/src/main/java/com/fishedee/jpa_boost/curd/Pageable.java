package com.fishedee.jpa_boost.curd;

import com.fishedee.jpa_boost.JPABoostException;

import java.util.List;
import java.util.Map;

/**
 * Created by fish on 2021/4/27.
 */
public interface Pageable {
    enum SortDirection{
        ASC,
        DESC;
        public static SortDirection valueFromString(String orderDirection){
            if( orderDirection.toLowerCase().equals("asc")){
                return ASC;
            }else if( orderDirection.toLowerCase().equals("desc")){
                return DESC;
            }else{
                throw new JPABoostException(1,"不合法的排序输入:"+orderDirection,null);
            }
        }
    }

    class SortInfo{
        private SortDirection direction;
        private String column;

        public SortInfo(SortDirection direction,String column){
            this.direction = direction;
            this.column = column;
        }

        public SortDirection getDirection(){
            return this.direction;
        }

        public String getColumn(){
            return this.column;
        }
    }

    int getPageIndex();

    int getPageSize();

    boolean shouldPage();

    boolean shouldCountAll();

    boolean shouldSort();

    List<SortInfo> getSort();
}
