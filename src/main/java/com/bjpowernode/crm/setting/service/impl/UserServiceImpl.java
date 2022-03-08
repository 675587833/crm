package com.bjpowernode.crm.setting.service.impl;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.setting.dao.UserDao;
import com.bjpowernode.crm.setting.domain.User;
import com.bjpowernode.crm.setting.service.UserService;
import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.SqlSessionUtil;

import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        User user = userDao.login(loginAct, loginPwd);
        if (user == null) {
            throw new LoginException("账号或者密码错误");
        }else {
            String expireTime = user.getExpireTime();
            String allowIps = user.getAllowIps();
            String lockState = user.getLockState();
            String sysTime = DateTimeUtil.getSysTime();
            if (sysTime.compareTo(expireTime) > 0) {
                throw new LoginException("账号已失效");
            } else if ("0".equals(lockState)) {
                throw new LoginException("账号已被锁定");
            } else if (!allowIps.contains(ip)) {
                throw new LoginException("IP地址已被禁用");
            }
        }
        return user;

    }

    @Override
    public List<User> getAllUser() {
        return userDao.getAllUser();
    }

    @Override
    public List<User> getUserList() {
        return userDao.getUserList();
    }
}
