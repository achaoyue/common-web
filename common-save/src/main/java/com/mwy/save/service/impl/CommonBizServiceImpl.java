package com.mwy.save.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mwy.base.util.SequenceUtils;
import com.mwy.save.modal.ParamConfigModal;
import com.mwy.save.reponstory.dao.StatementDao;
import com.mwy.save.reponstory.dao.modal.StatementDO;
import com.mwy.save.reponstory.dao.modal.StatementXmlDO;
import com.mwy.save.reponstory.mapper.CommonDataMapper;
import com.mwy.save.reponstory.mapper.StatementXmlMapper;
import com.mwy.save.service.CommonBizService;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mouwenyao 2020/8/22 7:49 下午
 */
@Service
public class CommonBizServiceImpl implements CommonBizService {
    private static final XMLLanguageDriver languageDriver = new XMLLanguageDriver();
    @Resource
    SqlSessionFactory sqlSessionFactory;
    @Resource
    StatementXmlMapper statementXmlMapper;
    @Resource
    StatementDao statementDao;

    @Resource
    private CommonDataMapper commonDataMapper;

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
        //必填校验
        paramConfigModals.stream().forEach(e->{
            if(Boolean.TRUE.equals(e.getRequired())
                    && param.get(e.getParamName())==null){
                throw new RuntimeException("参数必填:"+e.getParamPath());
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
            } else if ("seq".equals(e.getType())) {
                String parameter = map.getString(e.getParamPath());
                if (!StringUtils.isEmpty(parameter)) {
                    param.put(e.getParamName(), parameter);
                } else {
                    param.put(e.getParamName(), SequenceUtils.getSeqCode());
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

    @Override
    public Object refresh() {
        this.init();
        return "刷新完成";
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
            }else if("selectPlanSql".equals(statementDO.getType())){
                return commonDataMapper.select(statementDO.getStatementId(), param);
            }else if("updatePlanSql".equals(statementDO.getType())){
                return commonDataMapper.update(statementDO.getStatementId(), param);
            }else if("anySql".equals(statementDO.getType())){
                return commonDataMapper.select((String) param.get("sql"), param);
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
