package com.bjpowernode.crm.workbench.service;


import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;

public interface ClueService {
    boolean save(Clue c);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(List<ClueActivityRelation> carList);

    boolean convert(String clueId, Tran t,String createBy);
}
