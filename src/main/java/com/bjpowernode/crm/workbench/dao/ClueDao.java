package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;

public interface ClueDao {

    int save(Clue c);

    Clue detail(String id);

    Clue getById(String clueId);

    int delete(String clueId);
}
