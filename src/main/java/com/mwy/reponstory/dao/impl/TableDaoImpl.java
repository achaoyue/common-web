package com.mwy.reponstory.dao.impl;

import com.mwy.reponstory.dao.TableDao;
import com.mwy.reponstory.dao.modal.TableDO;
import com.mwy.reponstory.mapper.TableMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author mouwenyao 2020/8/23 9:00 上午
 */
@Service
public class TableDaoImpl implements TableDao {
    @Resource
    private TableMapper tableMapper;

    @Override
    public TableDO get(Long tableId, Long userId) {
        TableDO tableDO = new TableDO();
        tableDO.setId(tableId);
        tableDO.setUserId(userId);
        return tableMapper.selectOne(tableDO);
    }
}
