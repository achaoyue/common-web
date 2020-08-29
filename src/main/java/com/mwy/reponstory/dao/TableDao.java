package com.mwy.reponstory.dao;

import com.mwy.reponstory.dao.modal.TableDO;

/**
 * @author mouwenyao 2020/8/22 7:52 下午
 */
public interface TableDao {

    TableDO get(Long table, Long id);
}
