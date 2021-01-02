package com.mwy.save.reponstory.dao.modal;

import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

/**
 * @author mouwenyao 2020/12/12 10:37 下午
 */
@Table(name = "statement_xml")
@Data
public class StatementXmlDO {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private String xmlContent;
}
