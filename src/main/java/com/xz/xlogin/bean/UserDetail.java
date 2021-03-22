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
 * @Author: xz
 * @Date: 2020/11/26
 * <p>
 * 用户详细信息表
 */
@Entity
@Data
@DynamicUpdate
@Table(name = "user_detail")
@EntityListeners(AuditingEntityListener.class)
public class UserDetail implements Serializable {
    /**
     * 主键自动生成id
     */
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "system-uuid")
    @JsonIgnore//作用：在实体类向前台返回数据时用来忽略不想传递给前台的属性或接口。
    @Column(name = "id", length = 32)
    private String id;

    @OneToOne(mappedBy="detail")
    private User user;


    @Column(name = "avatar", length = 512)
    private String avatar;

    @Column(name = "nick", length = 32)
    private String nickName;

    @Column(name = "sex", length = 8)
    private String sex;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "slogan", length = 512)
    private String slogan;

    @Column(name = "city")
    private String city;


    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
