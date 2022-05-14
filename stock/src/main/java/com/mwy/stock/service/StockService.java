package com.mwy.stock.service;

import com.mwy.stock.modal.converter.StockConvertor;
import com.mwy.stock.reponstory.dao.StockDao;
import com.mwy.stock.reponstory.dao.modal.StockDO;
import com.mwy.stock.reponstory.remote.EasyMoneyRepository;
import com.mwy.stock.reponstory.remote.modal.EasyMoneyStockDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mouwenyao
 */
@Service
public class StockService {
    @Resource
    private StockDao stockDao;
    @Resource
    private EasyMoneyRepository easyMoneyRepository;

    public StockDO get(long id){
        StockDO stockDO = stockDao.selectById(id);
        return stockDO;
    }

    public void crowStock(){
        List<StockDO> dbStockDOs = stockDao.selectAll();
        Map<String, StockDO> stockDOMap = dbStockDOs.stream()
                .collect(Collectors.toMap(e -> e.getStockNum(), e -> e));

        List<EasyMoneyStockDTO> stockDTOList = easyMoneyRepository.getStockListByScript();

        List<StockDO> updateStockDos = stockDTOList.stream()
                .map(e -> StockConvertor.toDO(e))
                .filter(e->stockDOMap.containsKey(e.getStockNum()))
                .peek(e->e.setId(stockDOMap.get(e.getStockNum()).getId()))
                .collect(Collectors.toList());
        for (StockDO stockDO : updateStockDos) {
            stockDao.updateByIdSelective(stockDO);
        }

        List<StockDO> insertStockDOs = stockDTOList.stream()
                .map(e -> StockConvertor.toDO(e))
                .filter(e->!stockDOMap.containsKey(e.getStockNum()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(insertStockDOs)){
            stockDao.insertList(insertStockDOs);
        }

    }
}
