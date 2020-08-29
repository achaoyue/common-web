package com.mwy.reponstory.dao.modal;

import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author mouwenyao 2020/8/23 10:52 上午
 */
@Data
@Table(name = "sql_query")
public class SqlQueryDO {
    /**
     * id
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /**
     * user_id
     */
    private Long userId;

    /**
     * table_id
     */
    private Long tableId;

    /**
     * method
     */
    private String method;

    /**
     * sql
     */
    private String sqlText;

    /**
     * default_value
     */
    private String defaultValue;

}
