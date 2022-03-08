package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ContactsActivityRelation;

import java.util.List;

public interface ContactsActivityRelationDao {

    int save(List<ContactsActivityRelation> contactsActivityRelationList);
}
