package com.fishedee.jpa_boost.curd;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by fish on 2021/4/29.
 */
@Data
public class PageDTO {

    @NotNull
    @Min(0)
    @Max(500)
    private int pageIndex;

    @NotNull
    @Min(1)
    @Max(1000)
    private int pageSize;

    public CurdPageOffset getPageable(){
        return new CurdPageOffset(this.pageIndex,this.pageSize);
    }
}
