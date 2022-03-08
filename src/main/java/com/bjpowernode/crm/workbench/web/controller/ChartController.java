package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.util.JsonUtil;
import com.bjpowernode.crm.util.ServiceFactory;
import com.bjpowernode.crm.workbench.service.ChartService;
import com.bjpowernode.crm.workbench.service.impl.ChartServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet(urlPatterns = "/workbench/chart/transaction/getCharts.do")
public class ChartController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workbench/chart/transaction/getCharts.do".equals(path)){
            getCharts(request,response);
        }
    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {
        ChartService cs = (ChartService) ServiceFactory.getService(new ChartServiceImpl());
        Map<String,Object> map = cs.getCharts();
        JsonUtil.printJson(response,map);
    }
}
