package com.mwy.reponstory.dao.modal;

import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

/**
 * 用户表
 * @author mouwenyao 2020/8/22 7:21 下午
 */
@Table(name = "user")
@Data
public class UserDO {
    /**
     * id
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /**
     * 昵称
     */
    private String name;

    /**
     * 登录账号名
     */
    private String loginName;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 是否有效
     */
    private Integer status;
}
