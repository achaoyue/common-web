package com.mwy.base.co;

import lombok.Data;

import java.util.List;

/**
 * @author mouwenyao
 */
@Data
public class PageResult<T> {
    private int code;
    private String errMsg;
    private List<T> data;
    private int pageNum;
    private int pageSize;
    private int total;

    public static<T> PageResult<T> ofSuccess(List<T> data){
        PageResult<T> result = new PageResult<>();
        result.setCode(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }
    public static<T> PageResult<T> ofSuccess(List<T> data,int pageNum,int pageSize,int total){
        PageResult<T> result = new PageResult<>();
        result.setCode(ResultCode.SUCCESS);
        result.setData(data);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotal(total);
        return result;
    }
}
