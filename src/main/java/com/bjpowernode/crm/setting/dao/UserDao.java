package com.bjpowernode.crm.setting.dao;

import com.bjpowernode.crm.setting.domain.User;

import java.util.List;

public interface UserDao {
    User login(String loginAct, String loginPwd);

    List<User> getAllUser();

    List<User> getUserList();
}
