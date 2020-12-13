package com.mwy.reponstory.dao;

import com.mwy.reponstory.dao.modal.StatementDO;

/**
 * @author mouwenyao 2020/12/13 10:21 下午
 */
public interface StatementDao {

    StatementDO getByUniqKey(String uqKey);
}
