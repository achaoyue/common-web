package com.mwy;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.suggest.Suggester;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestNlp {

    @Test
    public void testSegment() {
        System.out.println( HanLP.segment("我们接下来写几个测试用例测试体验一把 ") );
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
        String content = "$王府井(SH600859)$  获得免税牌照两年了为何还没有落地？这是很多投资者的疑问，表面上是疫情影响进展，而实际上，在经济内循环的大背景下，免税概念光环正在逐渐消失！\n" +
                "\n" +
                "首先我们弄清楚什么是免税店？我们都知道免税店一般都设在海关，机场或旅游区。因为免税店卖的产品，一般都是没征收关税的，而这里指的关税是进口关税。也就是说免税店卖的主要是指国外进口到中国的产品。\n" +
                "\n" +
                "那为什么经济内循环背景下，免税店就没光环了呢？在过去的十年，中国赚取了大量的外汇，中国游客也成为全球买买买的主要力量，很多欧美品牌，都是中国人买出来的名牌。但是，疫情期间美元超发，作为制造业空心化的美国，超发的货币输入到了生产大国，特别是中国。也就是说，美国只管印钱，中国为代表的生产过通过加班加点换来美国的纸！虽然美元一直在升值，但实际上在美元超发情况下，已经极度贬值并失去公信力。\n" +
                "\n" +
                "今年俄乌冲突，俄罗斯率先让卢布铆定能源，这是对美元体系的公开挑战。今天印度宣布小麦禁止出口，实际上也是给印度卢比找新的铆定对象。同时，中国也在喊话RMB要寻找新的铆定对象....\n" +
                "\n" +
                "这些现象都说明一个事情，欧美靠印钱过日子的好时光，即将要结束了！\n" +
                "\n" +
                "那么，这跟我们的经济内循环有什么关系呢？当然有，而且关系巨大。我们不能再印钱，也不能让欧美印的钱大量输入继续推高通胀。所以，内循环就是在目前货币保有量情况下，让经济通过结构优化实现良心循环，在一定期间内消化通胀。\n" +
                "\n" +
                "实现内循环不但要防止通胀输入，同样资金也不能外流，比如出国旅游，购物等都尽量减少或避免了。大家都感觉到了防疫为什么越来越严格了吧？说白了，就是为了内循环！\n" +
                "\n" +
                "说到这里，大家可以理解标题的观点了吧，免税产品是进口产品，大部分都不是生产生活必需品，能不买就不买，避免造成外汇流失！这也是为什么大家一直等待的市内免税牌照，一直没发放的原因。\n" +
                "\n" +
                "回到王府井，散户们都寄予厚望，因为是唯一拿到免税牌照的百货公司。股票这东西，炒预期，2020年就超过了，现在要给予的期望，最多是超跌反弹！但是为什么王府井连个像样的反弹都没有呢？我在上一篇文章已经分析过了，总结起来就两个原因：巨量解禁+巨量减持。";
        List<String> keywordList = HanLP.extractKeyword(content, 10);
        System.out.println(keywordList);

        System.out.println(HanLP.getSummary(content, 100));
    }
}
