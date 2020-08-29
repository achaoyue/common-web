package com.mwy.reponstory.dao.impl;

import com.mwy.reponstory.dao.UserDao;
import com.mwy.reponstory.dao.modal.UserDO;
import com.mwy.reponstory.mapper.UserMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author mouwenyao 2020/8/23 8:59 上午
 */
@Service
public class UserDaoImpl implements UserDao {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDO get(String user) {
        UserDO userDO = new UserDO();
        userDO.setLoginName(user);
        return userMapper.selectOne(userDO);
    }
}
