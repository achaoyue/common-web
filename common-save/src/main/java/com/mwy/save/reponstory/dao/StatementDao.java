package com.mwy.save.reponstory.dao;

import com.mwy.save.reponstory.dao.modal.StatementDO;

/**
 * @author mouwenyao 2020/12/13 10:21 下午
 */
public interface StatementDao {

    StatementDO getByUniqKey(String uqKey);
}
