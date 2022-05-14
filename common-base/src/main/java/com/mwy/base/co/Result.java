package com.mwy.base.co;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String errMsg;
    private T data;

    public static<T> Result<T> ofSuccess(T data){
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }

}
