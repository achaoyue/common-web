package com.mwy.reponstory.dao.modal;

import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author mouwenyao 2020/8/22 7:45 下午
 */
@Table(name = "user_table")
@Data
public class TableDO {

    /**
     * id
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 作用描述
     */
    private String notice;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 状态
     */
    private Integer status;

}
