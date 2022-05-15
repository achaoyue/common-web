package com.mwy.base.qry;

import lombok.Data;

@Data
public class PageBaseQry extends BaseQry {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
