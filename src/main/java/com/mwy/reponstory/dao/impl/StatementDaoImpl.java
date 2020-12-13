package com.mwy.reponstory.dao.impl;

import com.mwy.reponstory.dao.StatementDao;
import com.mwy.reponstory.dao.modal.StatementDO;
import com.mwy.reponstory.mapper.StatementMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.WeekendSqls;

/**
 * @author mouwenyao 2020/12/13 10:21 下午
 */
@Service
public class StatementDaoImpl implements StatementDao {
    @Resource
    StatementMapper statementMapper;

    @Override
    public StatementDO getByUniqKey(String uqKey) {
        StatementDO query = new StatementDO();
        query.setUniqKey(uqKey);
        return statementMapper.selectOne(query);
    }
}
