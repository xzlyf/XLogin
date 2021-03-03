package com.xz.xlogin.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author: xz
 * @Date: 2020/12/3
 */
@Data
public class CreateEvent {

    /**
     * 短标题
     * 限制64字节
     */
    private String shortTitle;

    /**
     * 事件类容
     * 限制512字节
     */
    private String content;

    /**
     * 是否已完成
     */
    private boolean done;

    /**
     * 提醒时间
     */
    private Date remindTime;


}
