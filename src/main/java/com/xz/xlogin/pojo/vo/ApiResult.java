package com.xz.xlogin.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xz.xlogin.constant.StatusEnum;
import lombok.Data;

/**
 * 通用实体数据返回
 */
@Data
public class ApiResult {
    private int code;
    private String status;
    //如果没有数据传入，则过滤掉此输出
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object data;

    //如果没有数据传入，则过滤掉此输出
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message;

    public ApiResult() {
    }


    public ApiResult(StatusEnum status, Object data) {
        this.code = status.getCode();
        this.status = status.getMsg();
        this.data = data;
    }


    public ApiResult(StatusEnum status, Object data, String message) {
        this.code = status.getCode();
        this.status = status.getMsg();
        this.data = data;
        this.message = message;
    }

}
