package com.mwy.reponstory.dao.impl;

import com.mwy.reponstory.dao.QueryDao;
import com.mwy.reponstory.dao.modal.QueryDO;
import com.mwy.reponstory.mapper.QueryMapper;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author mouwenyao 2020/8/23 8:59 上午
 */
@Service
public class QueryDaoImpl implements QueryDao {
    @Resource
    private QueryMapper queryMapper;
    @Override
    public List<QueryDO> get(Long tableId, String method) {
        QueryDO queryDO = new QueryDO();
        queryDO.setTableId(tableId);
        queryDO.setMethodName(method);
        return queryMapper.select(queryDO);
    }
}
