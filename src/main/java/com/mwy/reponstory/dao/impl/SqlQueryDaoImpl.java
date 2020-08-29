package com.mwy.reponstory.dao.impl;

import com.mwy.reponstory.dao.SqlQueryDao;
import com.mwy.reponstory.dao.modal.SqlQueryDO;
import com.mwy.reponstory.mapper.SqlQueryMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author mouwenyao 2020/8/23 11:00 上午
 */
@Service
public class SqlQueryDaoImpl implements SqlQueryDao {
    @Resource
    private SqlQueryMapper sqlQueryMapper;

    @Override
    public SqlQueryDO get(Long userId, Long tableId, String method) {
        SqlQueryDO  sqlQueryDO = new SqlQueryDO();
        sqlQueryDO.setUserId(userId);
        sqlQueryDO.setTableId(tableId);
        sqlQueryDO.setMethod(method);
        return sqlQueryMapper.selectOne(sqlQueryDO);
    }
}
