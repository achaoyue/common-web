package com.mwy.reponstory.dao;

import com.mwy.reponstory.dao.modal.SqlQueryDO;

/**
 * @author mouwenyao 2020/8/23 10:59 上午
 */
public interface SqlQueryDao {
    SqlQueryDO get(Long userId,Long tableId,String method);
}
