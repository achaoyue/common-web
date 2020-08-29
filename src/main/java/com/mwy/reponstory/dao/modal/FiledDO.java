package com.mwy.reponstory.dao.modal;

import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

/**
 * 字段表
 * @author mouwenyao 2020/8/22 7:21 下午
 */
@Table(name = "filed")
@Data
public class FiledDO {
    /**
     * id
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    /**
     * 表id
     */
    private Long tableId;

    /**
     * 归属的表名
     */
    private String tableName;

    /**
     * 字段名
     */
    private String filed;

    /**
     * 字段备注
     */
    private String notice;

    /**
     * 是否必填
     */
    private Integer required;
}
