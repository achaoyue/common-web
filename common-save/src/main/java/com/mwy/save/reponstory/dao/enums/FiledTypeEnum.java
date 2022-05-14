package com.mwy.save.reponstory.dao.enums;

import com.mwy.base.util.db.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mouwenyao 2020/8/22 8:03 下午
 */
@Getter
@AllArgsConstructor
public enum FiledTypeEnum implements CodeEnum {
    CONDITION(1,"条件"),
    QUERY(2,"查询")
    ;
    int code;
    String desc;
}
