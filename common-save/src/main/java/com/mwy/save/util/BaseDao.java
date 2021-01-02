package com.mwy.save.util;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.annotation.Resource;
import org.apache.ibatis.session.RowBounds;
import org.springframework.context.ApplicationContext;

public class BaseDao<T,M extends MyBaseMapper> implements MyBaseMapper<T> {

    private MyBaseMapper<T> myBaseMapper;
    
    @Resource
    public void setApplicationContext(ApplicationContext applicationContext){
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class clazz = (Class<T>) type.getActualTypeArguments()[1];
        myBaseMapper = (MyBaseMapper<T>) applicationContext.getBean(clazz);
    }

    @Override
    public T selectOne(T t) {
        return myBaseMapper.selectOne(t);
    }

    @Override
    public List<T> select(T t) {
        return myBaseMapper.select(t);
    }

    @Override
    public List<T> selectAll() {
        return myBaseMapper.selectAll();
    }

    @Override
    public int selectCount(T t) {
        return myBaseMapper.selectCount(t);
    }

    @Override
    public T selectByPrimaryKey(Object o) {
        return myBaseMapper.selectByPrimaryKey(o);
    }

    @Override
    public boolean existsWithPrimaryKey(Object o) {
        return myBaseMapper.existsWithPrimaryKey(o);
    }

    @Override
    public int insert(T t) {
        return myBaseMapper.insert(t);
    }

    @Override
    public int insertSelective(T t) {
        return myBaseMapper.insertSelective(t);
    }

    @Override
    public int updateByPrimaryKey(T t) {
        return myBaseMapper.updateByPrimaryKey(t);
    }

    @Override
    public int updateByPrimaryKeySelective(T t) {
        return myBaseMapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public int delete(T t) {
        return myBaseMapper.delete(t);
    }

    @Override
    public int deleteByPrimaryKey(Object o) {
        return myBaseMapper.deleteByPrimaryKey(o);
    }

    @Override
    public List<T> selectByExample(Object o) {
        return myBaseMapper.selectByExample(o);
    }

    @Override
    public T selectOneByExample(Object o) {
        return myBaseMapper.selectOneByExample(o);
    }

    @Override
    public int selectCountByExample(Object o) {
        return myBaseMapper.selectCountByExample(o);
    }

    @Override
    public int deleteByExample(Object o) {
        return myBaseMapper.deleteByExample(o);
    }

    @Override
    public int updateByExample(T t, Object o) {
        return myBaseMapper.updateByExample(t, o);
    }

    @Override
    public int updateByExampleSelective(T t, Object o) {
        return myBaseMapper.updateByExampleSelective(t, o);
    }

    @Override
    public List<T> selectByExampleAndRowBounds(Object o, RowBounds rowBounds) {
        return myBaseMapper.selectByExampleAndRowBounds(o, rowBounds);
    }

    @Override
    public List<T> selectByRowBounds(T t, RowBounds rowBounds) {
        return myBaseMapper.selectByRowBounds(t, rowBounds);
    }

    @Override
    public int insertList(List<? extends T> list) {
        return myBaseMapper.insertList(list);
    }

    @Override
    public int insertUseGeneratedKeys(T t) {
        return myBaseMapper.insertUseGeneratedKeys(t);
    }
}
