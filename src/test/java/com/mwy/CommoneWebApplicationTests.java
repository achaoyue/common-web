package com.mwy;

import com.mwy.reponstory.mapper.CommonDataMapper;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CommoneWebApplication.class)
class CommoneWebApplicationTests {
    @Resource
    CommonDataMapper commonDataMapper;

    @Test
    void contextLoads() {
        String sql = "select * from user where login_name=#{param.login_name}";
        Map<String,String> param = new HashMap<>();
        param.put("login_name","mouwenyao");

        Object select = commonDataMapper.select(sql, param);
        System.out.println(select);
    }

}
