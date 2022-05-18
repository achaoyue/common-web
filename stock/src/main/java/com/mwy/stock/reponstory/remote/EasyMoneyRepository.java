package com.mwy.stock.reponstory.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mwy.base.util.HttpsUtils;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDayInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 东方财富爬虫
 *
 * @author mouwenyao
 */
@Slf4j
@Component
public class EasyMoneyRepository {

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
        param.put("beg", "0");
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

            stockBeanList.add(stockBean);
        }

        return stockBeanList;
    }


    public Double objectToBigDecimal(Object o) {
        try {
            return Double.valueOf(String.valueOf(o));
        } catch (NumberFormatException e) {
            return Double.valueOf(0);
        }
    }
}
