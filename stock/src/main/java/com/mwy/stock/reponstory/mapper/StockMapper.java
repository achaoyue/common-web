package com.mwy.stock.reponstory.mapper;

import com.mwy.base.util.db.MyBaseMapper;
import com.mwy.stock.reponstory.dao.modal.StockDO;
import com.mwy.stock.reponstory.dao.modal.UpDownSize;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMapper extends MyBaseMapper<StockDO> {

    int createTable();

    List<String> selectIndustry();

    UpDownSize queryUpDownSize(@Param("date") String date);

    List<UpDownSize> queryUpDownSizeByIndustry(@Param("date") String date);

    List<StockDO> queryTopByIndustry(@Param("date") String date);

    List<StockDO> queryUpTop(@Param("date") String date, @Param("size") int size);

    List<StockDO> queryDownTop(@Param("date") String date, @Param("size") int size);

    List<UpDownSize> queryUpDownSizeByIndustryPeriod(@Param("startDate") String startDate,
                                                     @Param("endDate") String endDate);
}
