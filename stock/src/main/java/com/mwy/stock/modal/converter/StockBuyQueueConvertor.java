package com.mwy.stock.modal.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mwy.stock.modal.dto.StockBuyQueueDTO;
import com.mwy.stock.modal.dto.StockBuyQueueDetailDTO;
import com.mwy.stock.reponstory.dao.modal.StockBuyQueueDO;

import java.util.Map;

public class StockBuyQueueConvertor {
    public static StockBuyQueueDTO toDTO(StockBuyQueueDO queueDO){
        if (queueDO == null) {
            return null;
        }
        StockBuyQueueDTO buyQueueDTO = new StockBuyQueueDTO();
        buyQueueDTO.setId(queueDO.getId());
        buyQueueDTO.setStockNum(queueDO.getStockNum());
        buyQueueDTO.setDetail(JSON.parseObject(queueDO.getDetail(),new TypeReference<Map<String, StockBuyQueueDetailDTO>>(){}));
        return buyQueueDTO;
    }

    public static StockBuyQueueDO toDO(StockBuyQueueDTO stockBuyQueueDTO) {
        StockBuyQueueDO stockBuyQueueDO = new StockBuyQueueDO();
        stockBuyQueueDO.setId(stockBuyQueueDTO.getId());
        stockBuyQueueDO.setStockNum(stockBuyQueueDTO.getStockNum());
        stockBuyQueueDO.setDetail(JSON.toJSONString(stockBuyQueueDTO.getDetail()));
        return stockBuyQueueDO;
    }
}
