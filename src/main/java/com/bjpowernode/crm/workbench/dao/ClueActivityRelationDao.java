package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {

    int unbund(String id);

    int bund(List<ClueActivityRelation> carList);

    List<ClueActivityRelation> getByClueId(String clueId);

    int delete(String clueId);
}
