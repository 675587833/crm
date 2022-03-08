package com.bjpowernode.crm.setting.service;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.setting.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getAllUser();

    List<User> getUserList();
}
