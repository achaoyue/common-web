package com.mwy.stock.reponstory.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mwy.base.util.HttpsUtils;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDayInfoDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockFundDTO;
import com.mwy.stock.modal.dto.easymoney.StockQueue;
import com.mwy.stock.reponstory.dao.modal.StockTimeInfoDO;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;



/**
 * 东方财富爬虫
 *
 * @author mouwenyao
 */
@Slf4j
@Component
public class EasyMoneyRepository {

    /**
     * K线
     * @param stockNum
     * @return
     */
    public List<EasyMoneyStockDayInfoDTO> crawlStockDayInfoListByStockBean(String stockNum) {
        String url = "http://push2his.eastmoney.com/api/qt/stock/kline/get";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("fields1", "f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6");
        param.put("fields2", "f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf59%2Cf60%2Cf61");
        param.put("ut", "7eea3edcaed734bea9cbfc24409ed989");
        param.put("klt", "101");
        param.put("fqt", "1");
        param.put("secid", "1." + stockNum);
        if (stockNum.startsWith("6")) {
            param.put("secid", "1." + stockNum);
        } else if (stockNum.startsWith("00") || stockNum.startsWith("3")) {
            param.put("secid", "0." + stockNum);
        } else {
            param.put("secid", "1." + stockNum);
        }
        param.put("beg", "20220101");
        param.put("end", "20500000");
        String sendResult = HttpsUtils.doGetString(url, param);

        JSONObject resultJsonObj = JSON.parseObject(sendResult);
        if (resultJsonObj == null
                || resultJsonObj.getJSONObject("data") == null
                || resultJsonObj.getJSONObject("data").getJSONArray("klines") == null) {
            log.info("{},爬取K线失败。{}", stockNum, sendResult);
            return Collections.emptyList();
        }
        JSONArray klines = resultJsonObj.getJSONObject("data").getJSONArray("klines");

        List<EasyMoneyStockDayInfoDTO> list = new ArrayList<>();
        for (int i = 0; i < klines.size(); i++) {
            String kline = (String) klines.get(i);
            String[] item = kline.split(",");
            EasyMoneyStockDayInfoDTO stockDayInfo = new EasyMoneyStockDayInfoDTO();
            stockDayInfo.setStockNum(stockNum);
            stockDayInfo.setDate(item[0]);
            stockDayInfo.setOpen(objectToBigDecimal(item[1]));
            stockDayInfo.setClose(objectToBigDecimal(item[2]));
            stockDayInfo.setHigh(objectToBigDecimal(item[3]));
            stockDayInfo.setLow(objectToBigDecimal(item[4]));
            stockDayInfo.setVolume(objectToBigDecimal(item[5]));
            stockDayInfo.setAmount(objectToBigDecimal(item[6]));
            stockDayInfo.setAmplitude(objectToBigDecimal(item[7]));
            stockDayInfo.setUpDownRange(objectToBigDecimal(item[8]));
            stockDayInfo.setUpDownPrices(objectToBigDecimal(item[9]));
            stockDayInfo.setTurnOverrate(objectToBigDecimal(item[10]));
            if (objectToBigDecimal(item[9]) == null) {
                stockDayInfo.setPreClose(Double.valueOf(0));
            } else {
                if (objectToBigDecimal(item[2]) == null) {
                    log.info("---------------null-------------" + stockNum);
                }
                stockDayInfo.setPreClose(objectToBigDecimal(item[2]) == null ? 0 : objectToBigDecimal(item[2]) - (objectToBigDecimal(item[9])));
            }
            list.add(stockDayInfo);
        }

        return list;
    }

    /**
     * 资金流向
     * @param stockNum
     * @return
     */
    public EasyMoneyStockFundDTO getStockFund(String stockNum){
        String url = "http://push2.eastmoney.com/api/qt/stock/get";

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ut", "fa5fd1943c7b386f172d6893dbfba10b");
        param.put("fltt", "2");
        param.put("fid", "f3");
        param.put("fs", "m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048");
        param.put("fields", "f135,f136,f137,f138,f139,f141,f142,f144,f145,f147,f148,f140,f143,f146,f149,f193,f196,f194,f195,f197");
        param.put("invt", "2");
        if (stockNum.startsWith("6")) {
            param.put("secid", "1." + stockNum);
        } else if (stockNum.startsWith("00") || stockNum.startsWith("3")) {
            param.put("secid", "0." + stockNum);
        } else {
            param.put("secid", "1." + stockNum);
        }
        
        String sendResult = HttpsUtils.doGetString(url, param);
        JSONObject map = JSONObject.parseObject(sendResult).getJSONObject("data");

        EasyMoneyStockFundDTO stockBean = new EasyMoneyStockFundDTO();
        stockBean.setMainMoneyIn(objectToBigDecimal(map.get("f135")));
        stockBean.setMainMoneyOut(objectToBigDecimal(map.get("f136")));
        stockBean.setSuperBigMoneyIn(objectToBigDecimal(map.get("f138")));
        stockBean.setSuperBigMoneyOut(objectToBigDecimal(map.get("f139")));
        stockBean.setBigMoneyIn(objectToBigDecimal(map.get("f141")));
        stockBean.setBigMoneyOut(objectToBigDecimal(map.get("f142")));

        return stockBean;
    }

    /**
     * 全量股票
     * @return
     */
    public List<EasyMoneyStockDTO> getStockListByScript() {
        String url = "http://19.push2.eastmoney.com/api/qt/clist/get";

        List<EasyMoneyStockDTO> stockBeanList = new ArrayList();

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("pn", "1");
        param.put("pz", "6000");
        param.put("po", "1");
        param.put("np", "1");
        param.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
        param.put("fltt", "2");
        param.put("invt", "2");
        param.put("fid", "f3");
        param.put("fs", "m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048");
        param.put("fields", "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f26,f38,f39,f22,f11,f62,f128,f136,f115,f152,f100,f102,f103");
        param.put("invt", "2");

        String sendResult = HttpsUtils.doGetString(url, param);

        JSONObject data = new JSONObject();
        try {
            data = JSONObject.parseObject(sendResult).getJSONObject("data");
        } catch (JSONException e) {
            log.error("解析jsonb报错：" + data.toJSONString());
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        JSONArray diff = data.getJSONArray("diff");
        for (Object o : diff) {
            Map<String, Object> map = (Map) o;
            EasyMoneyStockDTO stockBean = new EasyMoneyStockDTO();
            String stockNum = (String) map.get("f12");
            if (!(stockNum.startsWith("60") || stockNum.startsWith("00"))) {
                log.info("非主板股票,忽略.{}", stockNum);
                continue;
            }
            stockBean.setStockNum(stockNum);
            stockBean.setStockName((String) map.get("f14"));
            stockBean.setTotalFlowShares(objectToBigDecimal(map.get("f39")));
            stockBean.setTotalShares(objectToBigDecimal(map.get("f38")));
            stockBean.setUpDownRange(objectToBigDecimal(map.get("f3")));
            stockBean.setTurnOverrate(objectToBigDecimal(map.get("f8")));
            stockBean.setUpDownPrices(objectToBigDecimal(map.get("f4")));
            stockBean.setOpen(objectToBigDecimal(map.get("f17")));
            stockBean.setClose(objectToBigDecimal(map.get("f2")));
            stockBean.setHigh(objectToBigDecimal(map.get("f15")));
            stockBean.setLow(objectToBigDecimal(map.get("f16")));
            stockBean.setPreClose(objectToBigDecimal(map.get("f18")));
            stockBean.setVolume(objectToBigDecimal(map.get("f5")));
            stockBean.setAmount(objectToBigDecimal(map.get("f6")));
            stockBean.setAmplitude(objectToBigDecimal(map.get("f7")));
            stockBean.setTotalMarketValue(objectToBigDecimal(map.get("f20")));
            stockBean.setFlowMarketValue(objectToBigDecimal(map.get("f21")));
            stockBean.setListingDate(String.valueOf(map.get("f26")));
            stockBean.setQuantityRelativeRatio(objectToBigDecimal(map.get("f10")));
            stockBean.setIndustry((String) map.get("f100"));
            stockBean.setPlate((String) map.get("f102"));
            stockBean.setBelongPlate((String) map.get("f103"));
            stockBean.setUpdateDate(new Date());
//            stockBean.setMainMoneyIn(objectToBigDecimal(map.get("f135")));
//            stockBean.setMainMoneyOut(objectToBigDecimal(map.get("f136")));
//            stockBean.setSuperBigMoneyIn(objectToBigDecimal(map.get("f138")));
//            stockBean.setSuperBigMoneyOut(objectToBigDecimal(map.get("f139")));
//            stockBean.setBigMoneyIn(objectToBigDecimal(map.get("f141")));
//            stockBean.setBigMoneyOut(objectToBigDecimal(map.get("f142")));
//            stockBean.setBuyOne(objectToBigDecimal(map.get("f20")));
//            stockBean.setBuyOnePrice(objectToBigDecimal(map.get("f19")));
//            stockBean.setBuyTwo(objectToBigDecimal(map.get("f18")));
//            stockBean.setBuyTwoPrice(objectToBigDecimal(map.get("f17")));
//            stockBean.setBuyThree(objectToBigDecimal(map.get("f`6")));
//            stockBean.setBuyThreePrice(objectToBigDecimal(map.get("f15")));
//            stockBean.setBuyFour(objectToBigDecimal(map.get("f14")));
//            stockBean.setBuyFourPrice(objectToBigDecimal(map.get("f13")));
//            stockBean.setBuyFive(objectToBigDecimal(map.get("f12")));
//            stockBean.setBuyFivePrice(objectToBigDecimal(map.get("f11")));
//
//            stockBean.setSoldOne(objectToBigDecimal(map.get("f40")));
//            stockBean.setSoldOnePrice(objectToBigDecimal(map.get("f39")));
//            stockBean.setSoldTwo(objectToBigDecimal(map.get("f38")));
//            stockBean.setSoldTwoPrice(objectToBigDecimal(map.get("f37")));
//            stockBean.setSoldThree(objectToBigDecimal(map.get("f36")));
//            stockBean.setSoldThreePrice(objectToBigDecimal(map.get("f35")));
//            stockBean.setSoldFour(objectToBigDecimal(map.get("f34")));
//            stockBean.setSoldFourPrice(objectToBigDecimal(map.get("f33")));
//            stockBean.setSoldFive(objectToBigDecimal(map.get("f32")));
//            stockBean.setSoldFivePrice(objectToBigDecimal(map.get("f31")));

            stockBeanList.add(stockBean);
        }

        return stockBeanList;
    }

    /**
     * 分时抓取
     * @param stockNum
     * @return
     */
    public List<StockTimeInfoDO> crawlStockTimeInfoList(String stockNum) {
        String url = "http://push2his.eastmoney.com/api/qt/stock/trends2/get";
        //String param = "begin=0&end=-1&select=time,price,volume";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("fields1", "f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6%2Cf7%2Cf8%2Cf9%2Cf10%2Cf11%2Cf12%2Cf13");
        param.put("fields2", "f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58");
        param.put("ut", "7eea3edcaed734bea9cbfc24409ed989");
        param.put("ndays", "1");
        param.put("iscr", "0");
        if (stockNum.startsWith("6")) {
            param.put("secid", "1." + stockNum);
        } else if (stockNum.startsWith("00") || stockNum.startsWith("3")) {
            param.put("secid", "0." + stockNum);
        } else {
            param.put("secid", "1." + stockNum);
        }
        String sendResult = HttpsUtils.doGetString(url, param);

        JSONObject data = new JSONObject();
        try {
            data = JSONObject.parseObject(sendResult).getJSONObject("data");
        } catch (JSONException e) {
            log.error("解析jsonb报错：" + data.toJSONString());
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        List<StockTimeInfoDO> stockTimeInfoList = new ArrayList<StockTimeInfoDO>();
        Double prevClose = objectToBigDecimal(data.get("prePrice"));
        Double sumPrice = 0D;
        JSONArray trends = data.getJSONArray("trends");
        for (int i = 0; i < trends.size(); i++) {

            StockTimeInfoDO stockTimeInfo = new StockTimeInfoDO();
            String trend = (String) trends.get(i);
            String[] item = trend.split(",");
            Double price = objectToBigDecimal(item[2]);
            sumPrice += price;
            String datetime = item[0];
            stockTimeInfo.setDate(datetime.split(" ")[0]);
            stockTimeInfo.setTime(datetime.split(" ")[1]);
            stockTimeInfo.setStockNum(stockNum);
            stockTimeInfo.setPrice(objectToBigDecimal(item[2]));
            stockTimeInfo.setAmount(objectToBigDecimal(item[6]));
            stockTimeInfo.setVolume(objectToBigDecimal(item[5]));
            stockTimeInfo.setUpDownRange((price-prevClose)/prevClose);
            stockTimeInfo.setAvgPrice(sumPrice/(i+1));
            stockTimeInfoList.add(stockTimeInfo);
        }
        return stockTimeInfoList;
    }


    public Double objectToBigDecimal(Object o) {
        try {
            return Double.valueOf(String.valueOf(o));
        } catch (NumberFormatException e) {
            return Double.valueOf(0);
        }
    }

    /**
     * 盘口
     * @param stockNum
     * @return
     */
    public StockQueue crowQueue(String stockNum){
        String url = "http://push2.eastmoney.com/api/qt/stock/get";


        Map<String, Object> param = new HashMap<String, Object>();
        param.put("pn", "1");
        param.put("pz", "6000");
        param.put("po", "1");
        param.put("np", "1");
        param.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
        param.put("fltt", "2");
        param.put("invt", "2");
        param.put("fid", "f3");
        param.put("fs", "m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23,m:0+t:81+s:2048");
        param.put("fields", "f120,f121,f122,f174,f175,f59,f163,f43,f57,f58,f169,f170,f46,f44,f51,f168,f47,f164,f116,f60,f45,f52,f50,f48,f167,f117,f71,f161,f49,f530,f135,f136,f137,f138,f139,f141,f142,f144,f145,f147,f148,f140,f143,f146,f149,f55,f62,f162,f92,f173,f104,f105,f84,f85,f183,f184,f185,f186,f187,f188,f189,f190,f191,f192,f107,f111,f86,f177,f78,f110,f262,f263,f264,f267,f268,f255,f256,f257,f258,f127,f199,f128,f198,f259,f260,f261,f171,f277,f278,f279,f288,f152,f250,f251,f252,f253,f254,f269,f270,f271,f272,f273,f274,f275,f276,f265,f266,f289,f290,f286,f285,f292,f293,f294,f295");
        param.put("invt", "2");
        param.put("secid", "1." + stockNum);
        if (stockNum.startsWith("6")) {
            param.put("secid", "1." + stockNum);
        } else if (stockNum.startsWith("00") || stockNum.startsWith("3")) {
            param.put("secid", "0." + stockNum);
        } else {
            param.put("secid", "1." + stockNum);
        }

        String sendResult = HttpsUtils.doGetString(url, param);

        JSONObject data = new JSONObject();
        try {
            data = JSONObject.parseObject(sendResult).getJSONObject("data");
        } catch (JSONException e) {
            log.error("解析jsonb报错：" + data.toJSONString());
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        Map<String, Object> map = (Map) data;
        StockQueue stockBean = new StockQueue();
        stockBean.setBuyOne(objectToBigDecimal(map.get("f20")));
        stockBean.setBuyOnePrice(objectToBigDecimal(map.get("f19")));
        stockBean.setBuyTwo(objectToBigDecimal(map.get("f18")));
        stockBean.setBuyTwoPrice(objectToBigDecimal(map.get("f17")));
        stockBean.setBuyThree(objectToBigDecimal(map.get("f16")));
        stockBean.setBuyThreePrice(objectToBigDecimal(map.get("f15")));
        stockBean.setBuyFour(objectToBigDecimal(map.get("f14")));
        stockBean.setBuyFourPrice(objectToBigDecimal(map.get("f13")));
        stockBean.setBuyFive(objectToBigDecimal(map.get("f12")));
        stockBean.setBuyFivePrice(objectToBigDecimal(map.get("f11")));

        stockBean.setSoldOne(objectToBigDecimal(map.get("f40")));
        stockBean.setSoldOnePrice(objectToBigDecimal(map.get("f39")));
        stockBean.setSoldTwo(objectToBigDecimal(map.get("f38")));
        stockBean.setSoldTwoPrice(objectToBigDecimal(map.get("f37")));
        stockBean.setSoldThree(objectToBigDecimal(map.get("f36")));
        stockBean.setSoldThreePrice(objectToBigDecimal(map.get("f35")));
        stockBean.setSoldFour(objectToBigDecimal(map.get("f34")));
        stockBean.setSoldFourPrice(objectToBigDecimal(map.get("f33")));
        stockBean.setSoldFive(objectToBigDecimal(map.get("f32")));
        stockBean.setSoldFivePrice(objectToBigDecimal(map.get("f31")));

        return stockBean;
    }

    public static void main(String[] args) {
        EasyMoneyRepository easyMoneyRepository = new EasyMoneyRepository();
        StockQueue sz000001 = easyMoneyRepository.crowQueue("605580");
        System.out.println(sz000001);
    }
}
