package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.setting.dao.UserDao;
import com.bjpowernode.crm.setting.domain.User;
import com.bjpowernode.crm.util.SqlSessionUtil;
import com.bjpowernode.crm.vo.PagiNation;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public boolean save(Activity activity) {
        return activityDao.save(activity) == 1;
    }

    @Override
    public PagiNation<Activity> pageList(Map<String, Object> map) {
        int total = activityDao.getTotalByCondition(map);
        List<Activity> dataList = activityDao.getAllByCondition(map);
        PagiNation<Activity> pagiNation = new PagiNation<>();
        pagiNation.setTotal(total);
        pagiNation.setDataList(dataList);
        return pagiNation;
    }

    @Override
    public boolean delete(String[] ids) {

        boolean flag = false;
        int count = activityRemarkDao.getRemarkByActId(ids);
        if(activityRemarkDao.delRemarkByActId(ids) == count && activityDao.delete(ids) == ids.length){
            flag = true;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {

        List<User> userList = userDao.getAllUser();
        Activity activity = activityDao.getActivityById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("userList",userList);
        map.put("activity",activity);
        return map;
    }

    @Override
    public boolean update(Activity activity) {
        return activityDao.update(activity) == 1;
    }

    @Override
    public Activity detail(String id) {

        return activityDao.detail(id);
    }

    @Override
    public List<ActivityRemark> remarkList(String activityId) {
        return activityRemarkDao.remarkList(activityId);
    }

    @Override
    public boolean deleteRemark(String id) {
        return activityRemarkDao.deleteRemark(id) == 1;
    }

    @Override
    public Map<String, Object> saveRemark(ActivityRemark ar) {
        Map<String, Object> map  = new HashMap<>();
        map.put("success",activityRemarkDao.saveRemark(ar) == 1);
        map.put("r",ar);
        return map ;
    }

    @Override
    public Map<String, Object> updateRemark(ActivityRemark ar) {
        Map<String, Object> map  = new HashMap<>();
        map.put("success",activityRemarkDao.updateRemark(ar) == 1);
        map.put("r",ar);
        return map ;

    }

    @Override
    public List<Activity> showActivityList(String clueId) {
        return activityDao.showActivityList(clueId);
    }

    @Override
    public List<Activity> getActivityListByName(String name) {
        return activityDao.getActivityListByName(name);
    }

    @Override
    public List<Activity> getActivityListByNameAndNotClueId(String name, String clueId) {
        return activityDao.getActivityListByNameAndNotClueId(name,clueId);
    }
}
