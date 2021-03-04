package com.xz.xlogin.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: xz
 * @Date: 2020/11/23
 * <p>
 * 用户可公开的数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPublicDataVO{

    private String userName;
    private String userNo;
}
