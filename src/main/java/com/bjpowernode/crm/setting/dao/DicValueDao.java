package com.bjpowernode.crm.setting.dao;

import com.bjpowernode.crm.setting.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getAll(String code);
}
