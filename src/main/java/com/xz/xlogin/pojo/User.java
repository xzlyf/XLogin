package com.xz.xlogin.pojo;

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
@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "user_no", length = 16)
    private String userNo;

    @Column(name = "user_name", length = 32)
    private String userName;

    @JsonIgnore
    @Column(name = "user_pwd", length = 32)
    private String userPwd;

    @Column(name = "user_phone", length = 16)
    private String userPhone;

    @JsonIgnore
    @Column(name = "token", length = 32)
    private String token;


    //User是关系的维护端，当删除 User，会级联删除
    //@OneToOne(cascade=CascadeType.ALL)
    //@JoinColumn(name = "detail_id", referencedColumnName = "id")//唯一外键
    //private UserDetail userDetail;

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}