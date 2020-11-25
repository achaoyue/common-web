package com.mwy.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.mwy.component.MapUtils;
import com.mwy.reponstory.dao.FiledDao;
import com.mwy.reponstory.dao.QueryDao;
import com.mwy.reponstory.dao.SqlQueryDao;
import com.mwy.reponstory.dao.TableDao;
import com.mwy.reponstory.dao.UserDao;
import com.mwy.reponstory.dao.modal.SqlQueryDO;
import com.mwy.reponstory.dao.modal.TableDO;
import com.mwy.reponstory.dao.modal.UserDO;
import com.mwy.reponstory.mapper.CommonDataMapper;
import com.mwy.service.CommonBizService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

/**
 * @author mouwenyao 2020/8/22 7:49 下午
 */
@Service
public class CommonBizServiceImpl implements CommonBizService {
    private static final XMLLanguageDriver languageDriver = new XMLLanguageDriver();
    @Resource
    UserDao userDao;
    @Resource
    TableDao tableDao;
    @Resource
    CommonDataMapper commonDataMapper;
    @Resource
    SqlQueryDao sqlQueryDao;
    @Resource
    SqlSessionFactory sqlSessionFactory;

    @Override
    public List<Map<String,String>> queryBySql(String user, Long tableId, String method, Map<Object, Object> param) {

        UserDO userDO = userDao.get(user);
        //权限校验，校验是否有操作表的权限
        TableDO tableDO = tableDao.get(tableId, userDO.getId());
        Preconditions.checkNotNull(tableDO,"没有权限");

        SqlQueryDO sqlQueryDO = sqlQueryDao.get(userDO.getId(), tableDO.getId(), method);

        Map<Object,Object> jsonObject = JSON.parseObject(sqlQueryDO.getDefaultValue(),new TypeReference<Map<Object,Object>>(){});
        MapUtils.merge(jsonObject,param);


        SqlSource sqlSource = languageDriver.createSqlSource(sqlSessionFactory.getConfiguration(), sqlQueryDO.getSqlText(), null);
        BoundSql boundSql = sqlSource.getBoundSql(ImmutableMap.of("param",jsonObject));

        Preconditions.checkArgument(boundSql.getSql().trim().startsWith("select"),"配置错误");

        return commonDataMapper.select(boundSql.getSql(),jsonObject);

    }

    @Override
    public Object save(String user, Long tableId, String method, Map<Object, Object> param) {

        UserDO userDO = userDao.get(user);
        //权限校验，校验是否有操作表的权限
        TableDO tableDO = tableDao.get(tableId, userDO.getId());
        Preconditions.checkNotNull(tableDO,"没有权限");

        SqlQueryDO sqlQueryDO = sqlQueryDao.get(userDO.getId(), tableDO.getId(), method);

        Map<Object,Object> jsonObject = JSON.parseObject(sqlQueryDO.getDefaultValue(),new TypeReference<Map<Object,Object>>(){});
        MapUtils.merge(jsonObject,param);

        SqlSource sqlSource = languageDriver.createSqlSource(sqlSessionFactory.getConfiguration(), sqlQueryDO.getSqlText(), null);
        BoundSql boundSql = sqlSource.getBoundSql(ImmutableMap.of("param",jsonObject));
        String sql = boundSql.getSql();
        boolean check = sql.trim().startsWith("insert")
            || sqlQueryDO.getSqlText().trim().startsWith("update")
            || sqlQueryDO.getSqlText().trim().startsWith("delete");
        Preconditions.checkArgument(check,"配置错误");

        commonDataMapper.insert(sql,jsonObject);
        return param;
    }
}
