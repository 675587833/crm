package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PagiNation;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean save(Activity activity);

    PagiNation<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> remarkList(String activityId);

    boolean deleteRemark(String id);

    Map<String, Object> saveRemark(ActivityRemark ar);

    Map<String, Object> updateRemark(ActivityRemark ar);

    List<Activity> showActivityList(String clueId);

    List<Activity> getActivityListByNameAndNotClueId(String name, String clueId);

    List<Activity> getActivityListByName(String name);
}
