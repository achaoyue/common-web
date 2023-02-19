package com.mwy;

import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDayInfoDTO;
import com.mwy.stock.reponstory.remote.EasyMoneyRepository;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDTO;
import com.mwy.stock.util.DateUtils;

import java.util.Date;
import java.util.List;

public class EasyMoneyTest {
    public static void main(String[] args) {
//        EasyMoneyRepository easyMoneyRepository = new EasyMoneyRepository();
//        List<EasyMoneyStockDTO> stockListByScript = easyMoneyRepository.getStockListByScript();
//        List<EasyMoneyStockDayInfoDTO> stockDayInfoDTOS = easyMoneyRepository.crawlStockDayInfoListByStockBean("000001");
//        System.out.println(stockDayInfoDTOS.get(stockDayInfoDTOS.size()-1).getDate());

        Date now = DateUtils.string2Date("2023-11-12 15:00:10", "yyyy-MM-dd HH:mm:ss");
        String nowTime = DateUtils.date2String(now,"HH:mm");
        boolean inTime = nowTime.compareTo("09:30") > 0;
        System.out.println(inTime);
        System.out.println(nowTime);
    }


}
