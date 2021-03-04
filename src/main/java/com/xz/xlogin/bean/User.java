package com.xz.xlogin.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类
 */

@Entity
@Data
@DynamicUpdate
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键自动生成uuid
     */
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "system-uuid")
    @JsonIgnore//作用：在实体类向前台返回数据时用来忽略不想传递给前台的属性或接口。
    @Column(name = "uuid", length = 32)
    private String uuid;

    @OneToOne(optional=false,cascade=CascadeType.ALL)
    @JoinColumn(name="detail_id")
    private UserDetail detail;

    @Column(name = "user_no", length = 16, unique = true,nullable = false)
    private String userNo;

    @JsonIgnore
    @Column(name = "user_pwd", length = 32,nullable = false)
    private String userPwd;

    @Column(name = "user_phone", length = 16, unique = true)
    private String userPhone;

    @Column(name = "user_email", length = 320, unique = true)
    private String userEmail;

    @JsonIgnore
    @Column(name = "token", length = 32, unique = true)
    private String token;

    @JsonIgnore
    @Column(name = "order_qq", length = 128, unique = true)
    private String orderQQ;

    @JsonIgnore
    @Column(name = "order_wx", length = 128, unique = true)
    private String orderWx;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}