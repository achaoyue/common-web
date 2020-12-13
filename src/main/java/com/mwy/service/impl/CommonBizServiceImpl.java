package com.mwy.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.mwy.component.MapUtils;
import com.mwy.component.modal.ParamConfigModal;
import com.mwy.reponstory.dao.FiledDao;
import com.mwy.reponstory.dao.QueryDao;
import com.mwy.reponstory.dao.SqlQueryDao;
import com.mwy.reponstory.dao.StatementDao;
import com.mwy.reponstory.dao.TableDao;
import com.mwy.reponstory.dao.UserDao;
import com.mwy.reponstory.dao.modal.SqlQueryDO;
import com.mwy.reponstory.dao.modal.StatementDO;
import com.mwy.reponstory.dao.modal.StatementXmlDO;
import com.mwy.reponstory.dao.modal.TableDO;
import com.mwy.reponstory.dao.modal.UserDO;
import com.mwy.reponstory.mapper.CommonDataMapper;
import com.mwy.reponstory.mapper.StatementMapper;
import com.mwy.reponstory.mapper.StatementXmlMapper;
import com.mwy.service.CommonBizService;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    @Resource
    StatementMapper statementMapper;
    @Resource
    StatementXmlMapper statementXmlMapper;
    @Resource
    StatementDao statementDao;

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

    @Override
    public Object exe(String uqKey, HttpServletRequest request){
        StatementDO statementDO = statementDao.getByUniqKey(uqKey);

        List<ParamConfigModal> paramConfigModals = JSON.parseObject(statementDO.getDefaultParam(),
            new TypeReference<List<ParamConfigModal>>() {
            });
        Map<String,Object> param = new HashMap<>();
        paramConfigModals.forEach(e->{
            if("list".equals(e.getType())){
                String[] parameterValues = request.getParameterValues(e.getParamPath());
                if(!(parameterValues == null || parameterValues.length == 0)){
                    param.put(e.getParamName(),parameterValues);
                }else if((parameterValues == null || parameterValues.length == 0) && e.getDefaultValue() != null){
                    param.put(e.getParamName(),e.getDefaultValue());
                }
            }else if("string".equals(e.getType())){
                String parameter = request.getParameter(e.getParamPath());

                if(!StringUtils.isEmpty(parameter)){
                    param.put(e.getParamName(),parameter);
                }else if(e.getDefaultValue() != null){
                    param.put(e.getParamName(),e.getDefaultValue());
                }
            }else if("listNumber".equals(e.getType())){
                String[] parameterValues = request.getParameterValues(e.getParamPath());
                if(!(parameterValues == null || parameterValues.length == 0)){
                    List<Object> paramList = Arrays.stream(parameterValues)
                        .map(element -> JSON.parse(element))
                        .collect(Collectors.toList());
                    param.put(e.getParamName(),paramList);
                }else if((parameterValues == null || parameterValues.length == 0) && e.getDefaultValue() != null){
                    param.put(e.getParamName(),e.getDefaultValue());
                }
            }else if("number".equals(e.getType())){
                String parameter = request.getParameter(e.getParamPath());

                if(!StringUtils.isEmpty(parameter)){
                    param.put(e.getParamName(),JSON.parse(parameter));
                }else if(e.getDefaultValue() != null){
                    param.put(e.getParamName(),e.getDefaultValue());
                }
            }
        });

        return execParam(statementDO, param);
    }

    @Override
    public Object exec(String id, JSONObject map){
        StatementDO statementDO = statementDao.getByUniqKey(id);
        List<ParamConfigModal> paramConfigModals = JSON.parseObject(statementDO.getDefaultParam(),
            new TypeReference<List<ParamConfigModal>>() {
            });
        Map<String,Object> param = new HashMap<>();
        paramConfigModals.forEach(e->{
            if("list".equals(e.getType())){
                JSONArray parameterValues = map.getJSONArray(e.getParamPath());

                if(!(parameterValues == null || parameterValues.size() == 0)){
                    param.put(e.getParamName(),parameterValues);
                }else if((parameterValues == null || parameterValues.size() == 0) && e.getDefaultValue() != null){
                    param.put(e.getParamName(),e.getDefaultValue());
                }
            }else if("string".equals(e.getType())){
                String parameter = map.getString(e.getParamPath());

                if(!StringUtils.isEmpty(parameter)){
                    param.put(e.getParamName(),parameter);
                }else if(e.getDefaultValue() != null){
                    param.put(e.getParamName(),e.getDefaultValue());
                }
            }else if("listNumber".equals(e.getType())){
                JSONArray parameterValues = map.getJSONArray(e.getParamPath());
                if(!(parameterValues == null || parameterValues.size() == 0)){
                    param.put(e.getParamName(),parameterValues);
                }else if((parameterValues == null || parameterValues.size() == 0) && e.getDefaultValue() != null){
                    param.put(e.getParamName(),e.getDefaultValue());
                }
            }else if("number".equals(e.getType())){
                String parameter = map.getString(e.getParamPath());
                if(!StringUtils.isEmpty(parameter)){
                    param.put(e.getParamName(),JSON.parse(parameter));
                }else if(e.getDefaultValue() != null){
                    param.put(e.getParamName(),e.getDefaultValue());
                }
            }
        });
        //必填校验
        paramConfigModals.stream().forEach(e->{
            if(Boolean.TRUE.equals(e.getRequired())
                && param.get(e.getParamName())==null){
                throw new RuntimeException("参数必填:"+e.getParamPath());
            }
        });

        return execParam(statementDO, param);
    }

    private Object execParam(StatementDO statementDO, Map<String, Object> param) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
            if("selectList".equals(statementDO.getType() )){
                return sqlSession.selectList(statementDO.getStatementId(),param);
            }else if("selectOne".equals(statementDO.getType() )){
                return sqlSession.selectOne(statementDO.getStatementId(),param);
            }else if("delete".equals(statementDO.getType() )){
                return sqlSession.delete(statementDO.getStatementId(),param);
            }else if("insert".equals(statementDO.getType() )){
                sqlSession.insert(statementDO.getStatementId(),param);
                return param;
            }else if("update".equals(statementDO.getType() )){
                return sqlSession.update(statementDO.getStatementId(),param);
            }
        } finally {

        }
        return "nothing exec";
    }

    @PostConstruct
    public void init(){
        List<StatementXmlDO> statementXmlDOS = statementXmlMapper.selectAll();
        statementXmlDOS.forEach(e->{
            String xmlContent = e.getXmlContent();

            new XMLMapperBuilder(new ByteArrayInputStream(xmlContent.getBytes()),
                sqlSessionFactory.getConfiguration(),
                e.getId()+"",
                sqlSessionFactory.getConfiguration().getSqlFragments()
                ).parse();
        });

    }
}
