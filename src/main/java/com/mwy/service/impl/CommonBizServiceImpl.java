package com.mwy.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Preconditions;
import com.mwy.component.SqlBuilder;
import com.mwy.reponstory.dao.FiledDao;
import com.mwy.reponstory.dao.QueryDao;
import com.mwy.reponstory.dao.SqlQueryDao;
import com.mwy.reponstory.dao.TableDao;
import com.mwy.reponstory.dao.UserDao;
import com.mwy.reponstory.dao.enums.FiledTypeEnum;
import com.mwy.reponstory.dao.modal.QueryDO;
import com.mwy.reponstory.dao.modal.SqlQueryDO;
import com.mwy.reponstory.dao.modal.TableDO;
import com.mwy.reponstory.dao.modal.UserDO;
import com.mwy.reponstory.mapper.CommonDataMapper;
import com.mwy.service.CommonBizService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author mouwenyao 2020/8/22 7:49 下午
 */
@Service
public class CommonBizServiceImpl implements CommonBizService {
    @Resource
    UserDao userDao;
    @Resource
    TableDao tableDao;
    @Resource
    QueryDao queryDao;
    @Resource
    FiledDao filedDao;
    @Resource
    CommonDataMapper commonDataMapper;
    @Resource
    SqlQueryDao sqlQueryDao;

    @Override
    public Object query(String user, Long tableId, String method, Map<String, String> param) {
        UserDO userDO = userDao.get(user);
        //权限校验，校验是否有操作表的权限
        TableDO tableDO = tableDao.get(tableId, userDO.getId());

        //查询字段表，获取需要查询的字段，和需要使用的条件
        List<QueryDO> queryDOS = queryDao.get(tableDO.getId(),method);

        List<QueryDO> conditions = queryDOS.stream()
            .filter(e -> e.getType() == FiledTypeEnum.CONDITION)
            .collect(Collectors.toList());

        List<QueryDO> queryFiledList = queryDOS.stream()
            .filter(e -> e.getType() == FiledTypeEnum.QUERY)
            .collect(Collectors.toList());

        //构造sql，
        String sql = SqlBuilder.buildQuerySql(tableDO.getTableName(), queryFiledList,conditions,param);


        //执行sql
        Object select = commonDataMapper.select(sql, param);

        //提取字段

        return select;
    }

    @Override
    public List<Map<String,String>> queryBySql(String user, Long tableId, String method, Map<String, String> param) {

        UserDO userDO = userDao.get(user);
        //权限校验，校验是否有操作表的权限
        TableDO tableDO = tableDao.get(tableId, userDO.getId());
        Preconditions.checkNotNull(tableDO,"没有权限");

        SqlQueryDO sqlQueryDO = sqlQueryDao.get(userDO.getId(), tableDO.getId(), method);
        Preconditions.checkArgument(sqlQueryDO.getSqlText().trim().startsWith("select"),"配置错误");

        Map<String,String> jsonObject = JSON.parseObject(sqlQueryDO.getDefaultValue(),new TypeReference<Map<String,String>>(){});
        jsonObject.putAll(param);
        param = jsonObject;

        return commonDataMapper.select(sqlQueryDO.getSqlText(),param);

    }

    @Override
    public Object save(String user, Long tableId, String method, Map<String, String> param) {

        UserDO userDO = userDao.get(user);
        //权限校验，校验是否有操作表的权限
        TableDO tableDO = tableDao.get(tableId, userDO.getId());
        Preconditions.checkNotNull(tableDO,"没有权限");

        SqlQueryDO sqlQueryDO = sqlQueryDao.get(userDO.getId(), tableDO.getId(), method);
        boolean check = sqlQueryDO.getSqlText().trim().startsWith("insert")
            || sqlQueryDO.getSqlText().trim().startsWith("update")
            || sqlQueryDO.getSqlText().trim().startsWith("delete");
        Preconditions.checkArgument(check,"配置错误");

        Map<String,String> jsonObject = JSON.parseObject(sqlQueryDO.getDefaultValue(),new TypeReference<Map<String,String>>(){});
        jsonObject.putAll(param);
        param = jsonObject;

        commonDataMapper.insert(sqlQueryDO.getSqlText(),param);


        return param;
    }
}
