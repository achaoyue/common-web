package com.mwy.reponstory.dao;

import com.mwy.reponstory.dao.modal.UserDO;

/**
 * @author mouwenyao 2020/8/22 7:20 下午
 */
public interface UserDao {

    /**
     * 根据登录
     * @param user
     * @return
     */
    UserDO get(String user);
}
