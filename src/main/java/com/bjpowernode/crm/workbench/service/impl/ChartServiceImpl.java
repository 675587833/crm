package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.util.SqlSessionUtil;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.service.ChartService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartServiceImpl implements ChartService {
    TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);

    @Override
    public Map<String, Object> getCharts() {
        int total = tranDao.getTotal();
        List<Map<String,Object>> dataList = (List<Map<String, Object>>) tranDao.getCharts();
        Map<String, Object> map =  new HashMap<>();
        map.put("total",total);
        map.put("dataList",dataList);
        return map ;
    }
}
