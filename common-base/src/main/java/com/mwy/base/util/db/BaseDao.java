package com.mwy.base.util.db;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.annotation.Resource;
import org.apache.ibatis.session.RowBounds;
import org.springframework.context.ApplicationContext;

public class BaseDao<T,M extends MyBaseMapper>  {

    private MyBaseMapper<T> myBaseMapper;
    
    @Resource
    public void setApplicationContext(ApplicationContext applicationContext){
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class clazz = (Class<T>) type.getActualTypeArguments()[1];
        myBaseMapper = (MyBaseMapper<T>) applicationContext.getBean(clazz);
    }

    public T selectOne(T t) {
        return myBaseMapper.selectOne(t);
    }

    public List<T> select(T t) {
        return myBaseMapper.select(t);
    }

    public List<T> selectAll() {
        return myBaseMapper.selectAll();
    }

    public int selectCount(T t) {
        return myBaseMapper.selectCount(t);
    }

    public T selectById(Long o) {
        return myBaseMapper.selectByPrimaryKey(o);
    }

    public boolean existsWithId(Long o) {
        return myBaseMapper.existsWithPrimaryKey(o);
    }

    public int insert(T t) {
        return myBaseMapper.insert(t);
    }

    public int insertSelective(T t) {
        return myBaseMapper.insertSelective(t);
    }

    public int updateById(T t) {
        return myBaseMapper.updateByPrimaryKey(t);
    }

    public int updateByIdSelective(T t) {
        return myBaseMapper.updateByPrimaryKeySelective(t);
    }

    public int delete(T t) {
        return myBaseMapper.delete(t);
    }

    public int deleteById(Object o) {
        return myBaseMapper.deleteByPrimaryKey(o);
    }

    public List<T> selectByExample(Object o) {
        return myBaseMapper.selectByExample(o);
    }

    public T selectOneByExample(Object o) {
        return myBaseMapper.selectOneByExample(o);
    }

    public int selectCountByExample(Object o) {
        return myBaseMapper.selectCountByExample(o);
    }

    public int deleteByExample(Object o) {
        return myBaseMapper.deleteByExample(o);
    }

    public int updateByExample(T t, Object o) {
        return myBaseMapper.updateByExample(t, o);
    }

    public int updateByExampleSelective(T t, Object o) {
        return myBaseMapper.updateByExampleSelective(t, o);
    }

    public List<T> selectByExampleAndRowBounds(Object o, RowBounds rowBounds) {
        return myBaseMapper.selectByExampleAndRowBounds(o, rowBounds);
    }

    public List<T> selectByRowBounds(T t, RowBounds rowBounds) {
        return myBaseMapper.selectByRowBounds(t, rowBounds);
    }

    public int insertList(List<? extends T> list) {
        return myBaseMapper.insertList(list);
    }

    public int insertUseGeneratedKeys(T t) {
        return myBaseMapper.insertUseGeneratedKeys(t);
    }
}
