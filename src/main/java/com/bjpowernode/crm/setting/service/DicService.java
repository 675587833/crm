package com.bjpowernode.crm.setting.service;

import com.bjpowernode.crm.setting.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicService {
    Map<String, List<DicValue>> getDicValue();
}
