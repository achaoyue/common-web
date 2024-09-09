package com.mwy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDayInfoDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockFundDTO;
import com.mwy.stock.reponstory.dao.modal.StockTimeInfoDO;
import com.mwy.stock.reponstory.remote.EasyMoneyRepository;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDTO;
import com.mwy.stock.util.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EasyMoneyTest {
    public static void main(String[] args) {
//        EasyMoneyRepository easyMoneyRepository = new EasyMoneyRepository();
//        List<EasyMoneyStockDTO> stockListByScript = easyMoneyRepository.getStockListByScript();
//        List<EasyMoneyStockDayInfoDTO> stockDayInfoDTOS = easyMoneyRepository.crawlStockDayInfoListByStockBean("000001");
//        System.out.println(stockDayInfoDTOS.get(stockDayInfoDTOS.size()-1).getDate());

//        Date now = DateUtils.string2Date("2023-11-12 15:00:10", "yyyy-MM-dd HH:mm:ss");
//        String nowTime = DateUtils.date2String(now,"HH:mm");
//        boolean inTime = nowTime.compareTo("09:30") > 0;
//        System.out.println(inTime);
//        System.out.println(nowTime);

        EasyMoneyRepository easyMoneyRepository = new EasyMoneyRepository();
//        List<EasyMoneyStockDTO> stockListByScript = easyMoneyRepository.getStockListByScript();
//        EasyMoneyStockFundDTO stockFund = easyMoneyRepository.getStockFund("603163");
//        System.out.println(JSON.toJSONString(stockFund));
//        System.out.println(JSON.toJSON(stockListByScript));
//        List<StockTimeInfoDO> stockTimeInfoDOS = easyMoneyRepository.crawlStockTimeInfoList("600388");
//        System.out.println(JSON.toJSON(stockTimeInfoDOS));

//        List<EasyMoneyStockDTO> stockListByScript = easyMoneyRepository.getStockListByScript();
//        System.out.println(JSON.toJSON(stockListByScript));

        EasyMoneyStockFundDTO stockFund = easyMoneyRepository.getStockFund("600104");
        System.out.println(stockFund);

        List<EasyMoneyStockDTO> stockListByScript = easyMoneyRepository.getStockListByScript();
        List collect = stockListByScript.stream()
                .filter(e -> e.getUpDownRange() > 7)
                .map(e->e.getStockName())
                .collect(Collectors.toList());
        System.out.println(JSON.toJSONString(collect, SerializerFeature.PrettyFormat));

    }


}
