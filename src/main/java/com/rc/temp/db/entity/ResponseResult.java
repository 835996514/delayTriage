package com.rc.temp.db.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> {
    private Integer code;

    private String msg;

    private T data;

    public ResponseResult(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }
}
