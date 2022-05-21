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
        String content = "4月份以来，我国经济下行压力进一步加大，工业生产有所放缓。但是，我国产业升级态势并没有改变，新动能持续发展壮大，产业转型驶入快车道，高质量发展迈上新台阶，经济发展长期向好的趋势并未改变。\n" +
                "\n" +
                "疫情在冲击传统产业的同时，催生并推动不少新产业新业态新模式成长壮大，新动能韧性彰显。一个典型的表现是，4月份高技术制造业增加值增速高于全部工业，投资也好于全部投资，这意味着作为畅通国民经济循环和构建新发展格局的关键一环，高技术制造业对工业经济的引领带动作用显著增强，资源要素正在加快向高技术产业集聚，代表先进和高端的新动能增长引擎作用继续显现。\n" +
                "\n" +
                "产业升级态势不变，表现为传统产业继续向数字化、信息化、智能化深度转型。在工业生产有所放缓的大背景下，计算机、通信及其他电子设备制造业仍保持较高增速，说明数字产业本身仍在快速发展，制造业数字化转型步伐加快。锚定智能制造主攻方向，我国制造业正加快数字化改造升级，数字技术与制造业正深度融合，不断激发出新活力。当前，工业互联网创新发展成效显著，“5G+工业互联网”渗透多个重点行业，我国创新应用水平已处于全球第一梯队。数字信息基础设施建设明显加快，不仅支撑了数字经济的快速发展，也在重构制造业的核心竞争力，支撑制造业发力中高端领域，进一步巩固了我国制造业高质量发展态势。\n" +
                "\n" +
                "产业升级态势不变，还表现为传统产业绿色化转型加速。当前，新能源汽车、太阳能电池等产量增速较高，新能源产品需求进一步扩大；清洁能源消费占比继续提升，单位GDP能耗继续下降。一大批新兴绿色低碳技术正加速迭代，成为传统产业向绿色低碳转型的核心动力，也是高排放产业优化升级的核心驱动力。能耗不断下降，能效不断提升，表明我国工业绿色低碳水平持续进步，能耗“双控”与“双碳”目标有效衔接，传统产业绿色化改造稳步推进。\n" +
                "\n" +
                "越是困难，越要坚定走产业升级之路。尽管疫情对我国经济运行造成较大冲击，但这种影响是短期的、外在的，我国经济转型升级趋势没有变。产业升级契合了当前我国经济社会的发展需求，特别是消费升级后人民群众的需求。产业升级是我国制造业提升核心竞争力的必然要求，也是我国制造业提升核心竞争力的外在表现。要坚信，我国工业体系齐全、配套能力强的特点不会变，产业升级的发展态势不会变，经济发展的长期向好趋势也不会变。 （本文来源：经济日报 作者：金观平）";
        List<String> keywordList = HanLP.extractKeyword(content, 20);
        System.out.println(keywordList);

        System.out.println(HanLP.getSummary(content, 40));

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
