package com.mwy.reponstory.dao.modal;

import com.mwy.reponstory.dao.enums.FiledTypeEnum;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author mouwenyao 2020/8/22 7:40 下午
 */
@Table(name = "query")
@Data
public class QueryDO {
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
     * 字段名
     */
    private String field;

    /**
     * 是否必填
     */
    private Integer required;

    /**
     * 备注
     */
    private String notice;

    /**
     * 方法名
     */
    private String methodName;

    private FiledTypeEnum type;

    private String defaultValue;
}
