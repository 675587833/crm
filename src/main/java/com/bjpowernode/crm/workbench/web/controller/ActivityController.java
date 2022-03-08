package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.setting.domain.User;
import com.bjpowernode.crm.setting.service.UserService;
import com.bjpowernode.crm.setting.service.impl.UserServiceImpl;
import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.JsonUtil;
import com.bjpowernode.crm.util.ServiceFactory;
import com.bjpowernode.crm.util.UUIDUtil;
import com.bjpowernode.crm.vo.PagiNation;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/workbench/activity/getCreateOwner.do",
        "/workbench/activity/save.do", "/workbench/activity/pageList.do",
        "/workbench/activity/delete.do", "/workbench/activity/getUserListAndActivity.do",
        "/workbench/activity/update.do", "/workbench/activity/detail.do",
        "/workbench/activity/remarkList.do", "/workbench/activity/deleteRemark.do",
        "/workbench/activity/saveRemark.do","/workbench/activity/updateRemark.do"})
public class ActivityController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workbench/activity/getCreateOwner.do".equals(path)) {
            doGetCreateOwner(request, response);
        } else if ("/workbench/activity/pageList.do".equals(path)) {
            doPageList(request, response);
        } else if ("/workbench/activity/getUserListAndActivity.do".equals(path)) {
            doGetUserListAndActivity(request, response);
        } else if ("/workbench/activity/detail.do".equals(path)) {
            doDetail(request, response);
        } else if ("/workbench/activity/remarkList.do".equals(path)) {
            doRemarkList(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workbench/activity/save.do".equals(path)) {
            doSave(request, response);
        } else if ("/workbench/activity/delete.do".equals(path)) {
            doDel(request, response);
        } else if ("/workbench/activity/update.do".equals(path)) {
            doUpdate(request, response);
        } else if ("/workbench/activity/deleteRemark.do".equals(path)) {
            doDeleteRemark(request, response);
        } else if ("/workbench/activity/saveRemark.do".equals(path)) {
            doSaveRemark(request, response);
        }else if ("/workbench/activity/updateRemark.do".equals(path)) {
            doUpdateRemark(request, response);
        }
    }

    private void doUpdateRemark(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        User user = (User) request.getSession().getAttribute("user");
        String editBy = user.getName();
        String editFlag = "1";
        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(editFlag);
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String,Object> map= as.updateRemark(ar);
        JsonUtil.printJson(response,map);

    }

    private void doSaveRemark(HttpServletRequest request, HttpServletResponse response) {
        String id = UUIDUtil.getUUID();
        String noteContent = request.getParameter("noteContent");
        String createTime = DateTimeUtil.getSysTime();
        User user = (User) request.getSession().getAttribute("user");
        String createBy = user.getName();
        String editFlag = "0";
        String activityId = request.getParameter("activityId");
        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setCreateTime(createTime);
        ar.setCreateBy(createBy);
        ar.setEditFlag(editFlag);
        ar.setActivityId(activityId);
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String,Object> map= as.saveRemark(ar);
        JsonUtil.printJson(response,map);
    }

    private void doDeleteRemark(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.deleteRemark(id);
        JsonUtil.printJson(response, flag);
    }

    private void doRemarkList(HttpServletRequest request, HttpServletResponse response) {
        String ActivityId = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> remarks = as.remarkList(ActivityId);
        JsonUtil.printJson(response, remarks);

    }

    private void doDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity a = as.detail(id);
        request.setAttribute("a", a);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request, response);

    }

    private void doUpdate(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String editTime = DateTimeUtil.getSysTime();
        HttpSession session = request.getSession(false);
        String editBy = "";
        if (session != null) {
            User user = (User) session.getAttribute("user");
            editBy = user.getName();
        }
        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.update(activity);
        JsonUtil.printJson(response, flag);

    }

    private void doGetUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String, Object> map = as.getUserListAndActivity(id);
        JsonUtil.printJson(response, map);
    }

    private void doDel(HttpServletRequest request, HttpServletResponse response) {
        String[] ids = request.getParameterValues("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.delete(ids);
        JsonUtil.printJson(response, flag);


    }

    private void doPageList(HttpServletRequest request, HttpServletResponse response) {

        int pageNo = Integer.valueOf(request.getParameter("pageNo"));
        int pageSize = Integer.valueOf(request.getParameter("pageSize"));
        int skipCount = (pageNo - 1) * pageSize;
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        PagiNation<Activity> pagiNation = service.pageList(map);
        JsonUtil.printJson(response, pagiNation);

    }

    private void doSave(HttpServletRequest request, HttpServletResponse response) {
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        HttpSession session = request.getSession(false);
        String createBy = "";
        if (session != null) {
            User user = (User) session.getAttribute("user");
            createBy = user.getName();
        }
        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = service.save(activity);
        JsonUtil.printJson(response, flag);


    }

    private void doGetCreateOwner(HttpServletRequest request, HttpServletResponse response) {
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getAllUser();
        JsonUtil.printJson(response, userList);

    }


}
