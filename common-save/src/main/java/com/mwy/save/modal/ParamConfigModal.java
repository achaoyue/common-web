package com.mwy.save.modal;

import lombok.Data;

/**
 * @author mouwenyao 2020/12/12 11:13 下午
 */
@Data
public class ParamConfigModal {
    String type;//list,string
    Object defaultValue;
    String paramPath;
    String paramName;
    Boolean required;
}
