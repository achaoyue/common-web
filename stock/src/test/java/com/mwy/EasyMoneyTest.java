package com.mwy;

import com.mwy.stock.reponstory.remote.EasyMoneyRepository;
import com.mwy.stock.reponstory.remote.modal.EasyMoneyStockDTO;

import java.util.List;

public class EasyMoneyTest {
    public static void main(String[] args) {
        EasyMoneyRepository easyMoneyRepository = new EasyMoneyRepository();
        List<EasyMoneyStockDTO> stockListByScript = easyMoneyRepository.getStockListByScript();
        System.out.println(stockListByScript);
    }
}
