package com.mwy.save.reponstory.dao.modal;

import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author mouwenyao 2020/12/12 10:37 下午
 */
@Table(name = "statement")
@Data
public class StatementDO {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private String uniqKey;

    private String statementId;

    private String type;

    private String defaultParam;
}
