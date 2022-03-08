package com.bjpowernode.crm.setting.service.impl;

import com.bjpowernode.crm.setting.dao.DicTypeDao;
import com.bjpowernode.crm.setting.dao.DicValueDao;
import com.bjpowernode.crm.setting.domain.DicType;
import com.bjpowernode.crm.setting.domain.DicValue;
import com.bjpowernode.crm.setting.service.DicService;
import com.bjpowernode.crm.util.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getDicValue() {
        Map<String,List<DicValue>> map = new HashMap<>();
        List<DicType> dicTypes = dicTypeDao.getAll();
        for (DicType dicType : dicTypes) {
            String code = dicType.getCode();
            List<DicValue> dicValues = dicValueDao.getAll(code);
            map.put(code,dicValues);
        }
        return map;
    }
}
