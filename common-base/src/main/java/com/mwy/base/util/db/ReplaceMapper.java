package com.mwy.base.util.db;

import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

@RegisterMapper
public interface ReplaceMapper<T> {

    @InsertProvider(type = ReplaceProvider.class, method = "dynamicSQL")
    int upsertList(List<T> list);

    @InsertProvider(type = ReplaceProvider.class, method = "dynamicSQL")
    int upsert(T o);
}
