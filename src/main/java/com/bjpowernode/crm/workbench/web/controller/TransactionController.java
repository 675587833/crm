package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.setting.domain.User;
import com.bjpowernode.crm.setting.service.UserService;
import com.bjpowernode.crm.setting.service.impl.UserServiceImpl;
import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.JsonUtil;
import com.bjpowernode.crm.util.ServiceFactory;
import com.bjpowernode.crm.util.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TransactionService;
import com.bjpowernode.crm.workbench.service.impl.CustomerServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.TransactionServiceImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/workbench/transaction/add.do", "/workbench/transaction/getCustomerName.do",
        "/workbench/transaction/save.do", "/workbench/transaction/detail.do",
        "/workbench/transaction/tranHistoryList.do", "/workbench/transaction/changeStage.do",})
public class TransactionController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workbench/transaction/add.do".equals(path)) {
            add(request, response);
        } else if ("/workbench/transaction/getCustomerName.do".equals(path)) {
            getCustomerName(request, response);
        } else if ("/workbench/transaction/save.do".equals(path)) {
            save(request, response);
        } else if ("/workbench/transaction/detail.do".equals(path)) {
            detail(request, response);
        } else if ("/workbench/transaction/tranHistoryList.do".equals(path)) {
            tranHistoryList(request, response);
        } else if ("/workbench/transaction/changeStage.do".equals(path)) {
            changStage(request, response);
        }
    }

    private void changStage(HttpServletRequest request, HttpServletResponse response) {
        String tranId = request.getParameter("tranId");
        String stage = request.getParameter("stage");
        String expectedDate = request.getParameter("expectedDate");
        String money = request.getParameter("money");
        String editBy = ((User) request.getSession(false).getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        Tran t = new Tran();
        t.setId(tranId);
        t.setStage(stage);
        t.setExpectedDate(expectedDate);
        t.setMoney(money);
        t.setEditBy(editBy);
        t.setEditTime(editTime);
        TransactionService ts = (TransactionService)ServiceFactory.getService(new TransactionServiceImpl());
        boolean success = ts.changeStage(t);
        Map<String,String> map = (Map<String, String>) request.getServletContext().getAttribute("sp");
        String possibility = map.get(stage);
        t.setPossibility(possibility);
        Map<String,Object> map2 = new HashMap<>();
        map2.put("success",success);
        map2.put("t",t);
        JsonUtil.printJson(response,map2);
    }

    private void tranHistoryList(HttpServletRequest request, HttpServletResponse response) {
        String tranId = request.getParameter("tranId");
        TransactionService ts = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        List<TranHistory> thList = ts.tranHistoryList(tranId);
        Map<String, String> map = (Map<String, String>) request.getServletContext().getAttribute("sp");
        for (TranHistory th : thList) {
            String stage = th.getStage();
            String possibility = map.get(stage);
            th.setPossibility(possibility);
        }
        JsonUtil.printJson(response, thList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        TransactionService ts = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        Tran t = ts.detail(id);
        ServletContext application = request.getServletContext();
        Map<String, String> map = (Map<String, String>) application.getAttribute("sp");
        String possibility = map.get(t.getStage());
        t.setPossibility(possibility);
        request.setAttribute("t", t);

        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createBy = ((User) request.getSession(false).getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String customerName = request.getParameter("customerName");
        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateBy(createBy);
        t.setCreateTime(createTime);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);
        TransactionService ts = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        boolean flag = ts.save(customerName, t);
        if (flag) {
            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
        }

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String> customerNameList = cs.getCustomerName(name);
        JsonUtil.printJson(response, customerNameList);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = us.getUserList();
        request.setAttribute("userList", userList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);

    }
}
