package com.mwy.base.util.db;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.context.ApplicationContext;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.util.List;

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

    public int upsertList(List<T> list){
        if (CollectionUtils.isEmpty(list)){
            return 0;
        }
        return myBaseMapper.upsertList(list);
    }

    public int upsert(T o){
        if (o == null) {
            return 0;
        }
        return myBaseMapper.upsert(o);
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

    public List<T> selectByExample(Example o) {
        return myBaseMapper.selectByExample(o);
    }

    public T selectOneByExample(Example o) {
        return myBaseMapper.selectOneByExample(o);
    }

    public int selectCountByExample(Example o) {
        return myBaseMapper.selectCountByExample(o);
    }

    public int deleteByExample(Example o) {
        return myBaseMapper.deleteByExample(o);
    }

    public int updateByExample(T t, Example o) {
        return myBaseMapper.updateByExample(t, o);
    }

    public int updateByExampleSelective(T t, Example o) {
        return myBaseMapper.updateByExampleSelective(t, o);
    }

    public List<T> selectByExampleAndRowBounds(Object o, RowBounds rowBounds) {
        return myBaseMapper.selectByExampleAndRowBounds(o, rowBounds);
    }

    public List<T> selectByRowBounds(T t, RowBounds rowBounds) {
        return myBaseMapper.selectByRowBounds(t, rowBounds);
    }

    public int insertList(List<? extends T> list) {
        if (CollectionUtils.isEmpty(list)){
            return 0;
        }
        return myBaseMapper.insertList(list);
    }

    public int insertUseGeneratedKeys(T t) {
        return myBaseMapper.insertUseGeneratedKeys(t);
    }
}
