package com.mwy;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.suggest.Suggester;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestNlp {

    @Test
    public void testSegment() {
        System.out.println( HanLP.segment("大奇迹日，嗨了，稳了，2.98买不了吃亏，买不了上当，干啊，散户就不要墨迹了，昨天我就说了，2.98，稳了，嗨了，抄底抄底抄底") );
    }

    @Test
    public void testSuggest() {
        Suggester suggester = new Suggester();
        String[] titleArray =
                (
                        "威廉王子发表演说 呼吁保护野生动物\n" +
                                "《时代》年度人物最终入围名单出炉 普京马云入选\n" +
                                "“黑格比”横扫菲：菲吸取“海燕”经验及早疏散\n" +
                                "日本保密法将正式生效 日媒指其损害国民知情权\n" +
                                "人工智能如今是非常火热的一门技术”"
                ).split("\\n");
        for (String title : titleArray)
        {
            suggester.addSentence(title);
        }

        System.out.println(suggester.suggest("机器学习", 1));   // 语义
        System.out.println(suggester.suggest("危机公共", 1));   // 字符
        System.out.println(suggester.suggest("mayun", 1));     // 拼音
    }

    @Test
    public void testKeyExtract() {
        String content = "覆盖县域的网格仓，其团点分布常存在这样的情况：集中于某个较远的乡镇有若干团点。这些团点若从网格仓直接派一台车，运配成本较高。出于降本考虑，网格仓尝试将上述团点以类似承包的方式交由镇上的某个快递站点等具备场地和配送能力的第三方（后称“服务站”），网格仓将该团点动销商品配送至服务站场地，由服务站人员完成团点配送。这个模式下，因服务站的配送工具多为三轮车，运维成本较低，从而实现成本节降。\n";
        List<String> keywordList = HanLP.extractKeyword(content, 10);
        System.out.println(keywordList);

        System.out.println(HanLP.getSummary(content, 20));

    }

    public static void main(String[] args) {
        //请求头中的token   需要进行替换成在官网申请的新token
        String token="38a4ffbbcdc140f094ac696b9258d5201652931975790token";
        //申请的接口地址
        String url="http://comdo.hanlp.com/hanlp/v1/textAnalysis/sentimentAnalysis";
        //处理分析的文本，作为params参数传入  support@hanlp.com
        Map<String,Object> params=new HashMap<String,Object>();

        //参数传入要处理分析的文本
        params.put("text","大奇迹日，嗨了，稳了，2.98买不了吃亏，买不了上当，干啊，散户就不要墨迹了，昨天我就说了，2.98，稳了，嗨了，抄底抄底抄底");

        //执行HanLP接口，result为返回的结果
        String result=doHanlpApi(token,url,params);
        //打印输出结果
        System.out.println(result);
    }
    public static String doHanlpApi(String token,String url,Map<String,Object> params) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            //添加header请求头，token请放在header里
            httpPost.setHeader("token", token);
            // 创建参数列表
            List<NameValuePair> paramList = new ArrayList<>();
            if (params != null) {
                for (String key : params.keySet()) {
                    //所有参数依次放在paramList中
                    paramList.add(new BasicNameValuePair(key, (String) params.get(key)));
                }
                //模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "utf-8");
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
            return resultString;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(response!=null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
