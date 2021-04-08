package com.xz.xlogin.bean;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author: xz
 * @Date: 2021/3/8
 */

@Entity
@Data
@DynamicUpdate
@Table(name = "app")
public class App implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "app_id", unique = true)
    private String appId;

    @Column(name = "app_secret")
    private String appSecret;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "app_version")
    private String appVersion;
}
