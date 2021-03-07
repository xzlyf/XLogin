package com.xz.xlogin.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: xz
 * @Date: 2021/3/7
 */
@Entity
@Data
@DynamicUpdate
@Table(name = "Identity")
public class Identity {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "system-uuid")
    @JsonIgnore//作用：在实体类向前台返回数据时用来忽略不想传递给前台的属性或接口。
    @Column(name = "id", length = 32)
    private String id;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;


    @Column(name = "app_id", unique = true, nullable = false)
    private String appId;

    @Column(name = "token", unique = true)
    private String token;

    @Column(name = "last_login_time")
    private Date lastLoginTime;
}
