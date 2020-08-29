package com.mwy.reponstory.dao;

import com.mwy.reponstory.dao.modal.QueryDO;
import java.util.List;

/**
 * @author mouwenyao 2020/8/22 7:59 下午
 */
public interface QueryDao {

    List<QueryDO> get(Long id, String method);
}
