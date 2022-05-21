package com.mwy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/mouwenyao/person/mwy_log.log"));
        List<String> traceIds = new ArrayList<>();
        List<String> deliveryOrgCodes = new ArrayList<>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null){
            int i = line.indexOf("info:{");
            line = line.substring(i+5).replace(" | is_security:","");


            JSONObject jsonObjectx = JSON.parseObject(line);
            List<String> list = new ArrayList<>();
            list.addAll(jsonObjectx.keySet());
            String key = list.get(0);
            JSONObject jsonObject = jsonObjectx.getJSONObject(key);
            if (!jsonObject.getString("oldGridWarehouseOrgCode").equals(jsonObject.getString("newGridWarehouseOrgCode"))){
                System.out.println(key+"--"+line);
                traceIds.add(line);
            }
            deliveryOrgCodes.add(jsonObject.getString("deliveryOrgCode"));
//            deliveryOrgCodes.add(key);
        }
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        traceIds.forEach(e->{
            System.out.println(e);
        });

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        deliveryOrgCodes.forEach(e->{
            System.out.println(e);
        });

    }
}
