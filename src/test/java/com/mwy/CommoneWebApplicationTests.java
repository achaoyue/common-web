package com.mwy;

import com.mwy.reponstory.mapper.CommonDataMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.Data;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CommoneWebApplication.class)
class CommoneWebApplicationTests {
    private static final XMLLanguageDriver languageDriver = new XMLLanguageDriver();
    @Resource
    CommonDataMapper commonDataMapper;
    @Resource
    SqlSessionFactory factory;

    @Test
    void contextLoads() {
        String sql = "select * from user where login_name=#{param.login_name}";
        Map<Object,Object> param = new HashMap<>();
        param.put("login_name","mouwenyao");

        Object select = commonDataMapper.select(sql, param);
        System.out.println(select);
    }

    @Test
    public void  test() throws IOException {
        Configuration configuration = factory.getConfiguration();
        SqlSource sqlSource = languageDriver
            .createSqlSource(configuration, "<script>SELECT * FROM user WHERE id = \\#{userId}"
                + "<if test='a==1'>"
                + "and name='mouwenyao123'"
                + "</if>"
                + "</script>", null);

        Map<String,Object > param = new HashMap<>();
        param.put("userId","3");
        param.put("a",1);
        BoundSql boundSql = sqlSource.getBoundSql(param);
        String sql = boundSql.getSql();
        Object parameterObject = boundSql.getParameterObject();

//        ResultMap inlineResultMap = new ResultMap.Builder(
//            configuration,
//            "mwy_test",
//            HashMap.class,
//            new ArrayList<ResultMapping>(),
//            null).build();
//
//        System.out.println(sql);
//        MappedStatement mwy_test = new Builder(configuration, "mwy_test", sqlSource, SqlCommandType.SELECT)
//            .resultMaps(Arrays.asList(inlineResultMap))
//            .build();
//        configuration.addMappedStatement(mwy_test);
//
//
//        SqlSession sqlSession = factory.openSession();
//        List<Object> objects = sqlSession.selectList("mwy_test", param);


//        System.out.println(objects);

    }

}
