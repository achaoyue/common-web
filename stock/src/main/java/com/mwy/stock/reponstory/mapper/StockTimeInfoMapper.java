package com.mwy.stock.reponstory.mapper;

import com.mwy.base.util.db.MyBaseMapper;
import com.mwy.stock.modal.co.CommonPair;
import com.mwy.stock.reponstory.dao.modal.StockTimeInfoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTimeInfoMapper {

    int createTable(@Param("stockNum") String stockNum);

    int deleteByTable(@Param("stockNum") String stockNum, @Param("date") String date);

    int insertByTable(@Param("timeInfoDO") StockTimeInfoDO timeInfoDO, @Param("stockNum") String stockNum);

    int insertByTables(@Param("timeInfoDOs") List<StockTimeInfoDO> timeInfoDO, @Param("stockNum") String stockNum);

    double abnormal(@Param("stockNum") String stockNum, @Param("dates") List<String> date);

    List<StockTimeInfoDO> selectByDate(@Param("stockNum") String stockNum, @Param("date") String date);

    String maxDate(@Param("stockNum") String stockNum);

    List<CommonPair> abnormalPeriod(@Param("stockNum") String stockNum, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
