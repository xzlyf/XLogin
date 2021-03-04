package com.xz.xlogin.bean.vo;

import lombok.Data;

/**
 * @Author: xz
 * @Date: 2020/12/6
 */
@Data
public class AppUpdateVo {

    private Integer versionCode;
    private String versionName;
    private String updateMsg;
    private String downloadKey;
    private String md5;
    private String fileLength;

}
