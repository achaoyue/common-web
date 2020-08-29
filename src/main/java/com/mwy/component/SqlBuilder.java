package com.mwy.component;

import com.mwy.reponstory.dao.enums.FiledTypeEnum;
import com.mwy.reponstory.dao.modal.QueryDO;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.util.Strings;

/**
 * @author mouwenyao 2020/8/22 8:07 下午
 */
public class SqlBuilder {

    public static String buildQuerySql(String tableName, List<QueryDO> queryFiledList, List<QueryDO> conditions, Map<String, String> param) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select ");
        for (QueryDO queryDO : queryFiledList) {
            stringBuilder.append("`").append(queryDO.getField()).append("`, ");
        }
        stringBuilder.delete(stringBuilder.length()-2,stringBuilder.length());
        stringBuilder.append(" from ").append(tableName).append(" ");
        stringBuilder.append(" where ");
        String preFix = "";
        for (QueryDO condition : conditions) {
            String value = param.get(condition.getField());
            if(value == null && Strings.isEmpty(condition.getDefaultValue())){
                continue;
            }

            stringBuilder.append(preFix).append("`").append(condition.getField()).append("`").append("=");
            if(Strings.isNotEmpty(condition.getDefaultValue())){
                stringBuilder.append("'").append(condition.getDefaultValue()).append("'");
                continue;
            }else{
                if(value != null){
                    stringBuilder.append("#{").append("param.").append(condition.getField()).append("}");
                }
            }
            preFix = " and ";
        }

        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        String tableName = "user";
        QueryDO queryDO = new QueryDO();
        queryDO.setType(FiledTypeEnum.QUERY);
        queryDO.setField("name");
        List<QueryDO> queryDOList = Arrays.asList(queryDO);

        QueryDO condition1 = new QueryDO();
        condition1.setField("pwd");
        QueryDO condition2 = new QueryDO();
        condition2.setField("age");
        condition2.setDefaultValue("1");
        QueryDO condition3 = new QueryDO();
        condition3.setField("class");

        List<QueryDO> condition = Arrays.asList(condition1,condition2,condition3);

        Map<String,String > param = new HashMap<>();
        param.put("pwd","11111");
        param.put("class","cl");
        param.put("bbbbb","cl");

        String sql = buildQuerySql(tableName, queryDOList, condition, param);
        System.out.println(sql);
    }
}
