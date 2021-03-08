package com.xz.xlogin.bean;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @Author: xz
 * @Date: 2021/3/8
 */

@Entity
@Data
@DynamicUpdate
@Table(name = "app")
public class App {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "app_version")
    private String appVersion;
}
