package com.mwy;

import com.mwy.save.reponstory.mapper.CommonDataMapper;
import com.mwy.starter.CommoneWebApplication;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CommoneWebApplication.class)
class CommoneWebApplicationTests {
    private static final XMLLanguageDriver languageDriver = new XMLLanguageDriver();
    @Resource
    CommonDataMapper commonDataMapper;
    @Resource
    SqlSessionFactory factory;

}
