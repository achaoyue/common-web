package com.mwy.reponstory.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

/**
 * @author mouwenyao 2020/8/23 8:35 上午
 */
@Repository
public interface CommonDataMapper {

    /**
     * 查询
     * @param sql
     * @param param
     * @return
     */
    @Select("${sql}")
    List<Map<String,String>> select(@Param("sql") String sql,@Param("param") Map<String,String> param);

    @Insert("${sql}")
    @SelectKey(statement="SELECT LAST_INSERT_ID()",keyColumn = "id" ,keyProperty="param.id", before=false, resultType=String.class)
    Integer insert(@Param("sql")String sqlText,@Param("param") Map<String, String> param);
}
