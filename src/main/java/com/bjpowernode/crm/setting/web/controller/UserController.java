package com.bjpowernode.crm.setting.web.controller;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.setting.domain.User;
import com.bjpowernode.crm.setting.service.UserService;
import com.bjpowernode.crm.setting.service.impl.UserServiceImpl;
import com.bjpowernode.crm.util.JsonUtil;
import com.bjpowernode.crm.util.MD5Util;
import com.bjpowernode.crm.util.ServiceFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = "/setting/login.do")
public class UserController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletPath();
        if ("/setting/login.do".equals(path)) {
            doLogin(request, response);
        }
    }

    private void doLogin(HttpServletRequest request, HttpServletResponse response) {
        String loginAct = request.getParameter("loginAct");
        String loginPwd = MD5Util.getMD5(request.getParameter("loginPwd"));
        String ip = request.getRemoteAddr();
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        try {
            User user = userService.login(loginAct, loginPwd, ip);
            request.getSession().setAttribute("user", user);
            JsonUtil.printJson(response,true);
        } catch (LoginException e) {
            JsonUtil.printJson(response, e.getMessage());
        }

    }
}
