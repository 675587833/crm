package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.SqlSessionUtil;
import com.bjpowernode.crm.util.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;

import java.util.ArrayList;
import java.util.List;

public class ClueServiceImpl implements ClueService {
    ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
    ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public boolean save(Clue c) {
        return clueDao.save(c) == 1;
    }

    @Override
    public Clue detail(String id) {
        return clueDao.detail(id);
    }

    @Override
    public boolean unbund(String id) {
        return clueActivityRelationDao.unbund(id) == 1;
    }

    @Override
    public boolean bund(List<ClueActivityRelation> clueActivityRelationList) {
        return clueActivityRelationDao.bund(clueActivityRelationList) == clueActivityRelationList.size();
    }

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {
        boolean success = true;
        String createTime = DateTimeUtil.getSysTime();
        Clue c = clueDao.getById(clueId);
        String company = c.getCompany();
        Customer cus = customerDao.getByName(company);
        if (cus == null) {
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setOwner(c.getOwner());
            cus.setName(c.getCompany());
            cus.setWebsite(c.getWebsite());
            cus.setPhone(c.getPhone());
            cus.setCreateBy(createBy);
            cus.setCreateTime(createTime);
            cus.setContactSummary(c.getContactSummary());
            cus.setNextContactTime(c.getNextContactTime());
            cus.setDescription(c.getDescription());
            cus.setAddress(c.getAddress());
            if (customerDao.save(cus) != 1) {
                success = false;
            }
        }

        Contacts con = new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setOwner(c.getOwner());
        con.setSource(c.getSource());
        con.setCustomerId(cus.getId());
        con.setFullname(c.getFullname());
        con.setAppellation(c.getAppellation());
        con.setEmail(c.getEmail());
        con.setMphone(c.getMphone());
        con.setJob(c.getJob());
        con.setCreateBy(createBy);
        con.setCreateTime(createTime);
        con.setDescription(c.getDescription());
        con.setContactSummary(c.getContactSummary());
        con.setNextContactTime(c.getNextContactTime());
        con.setAddress(c.getAddress());
        if (contactsDao.save(con) != 1) {
            success = false;
        }

        List<ClueRemark> clueRemarkList = clueRemarkDao.getByClueId(clueId);
        if (clueRemarkList.size() != 0) {
            List<CustomerRemark> customerRemarkList = new ArrayList<>();
            List<ContactsRemark> contactsRemarkList = new ArrayList<>();
            for (ClueRemark clueRemark : clueRemarkList) {
                String noteContent = clueRemark.getNoteContent();
                CustomerRemark customerRemark = new CustomerRemark();
                customerRemark.setId(UUIDUtil.getUUID());
                customerRemark.setNoteContent(clueRemark.getNoteContent());
                customerRemark.setCreateBy(createBy);
                customerRemark.setCreateTime(createTime);
                customerRemark.setEditFlag("0");
                customerRemark.setCustomerId(cus.getId());
                customerRemarkList.add(customerRemark);

                ContactsRemark contactsRemark = new ContactsRemark();
                contactsRemark.setId(UUIDUtil.getUUID());
                contactsRemark.setNoteContent(clueRemark.getNoteContent());
                contactsRemark.setCreateBy(createBy);
                contactsRemark.setCreateTime(createTime);
                contactsRemark.setEditFlag("0");
                contactsRemark.setContactsId(con.getId());
                contactsRemarkList.add(contactsRemark);
            }

            if (customerRemarkDao.save(customerRemarkList) != clueRemarkList.size()) {
                success = false;
            }

            if (contactsRemarkDao.save(contactsRemarkList) != contactsRemarkList.size()) {
                success = false;
            }
        }

        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getByClueId(clueId);
        if (clueActivityRelationList.size() != 0) {
            List<ContactsActivityRelation> contactsActivityRelationList = new ArrayList<>();
            for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
                ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
                contactsActivityRelation.setId(UUIDUtil.getUUID());
                contactsActivityRelation.setContactsId(con.getId());
                contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
                contactsActivityRelationList.add(contactsActivityRelation);
            }

            if (contactsActivityRelationDao.save(contactsActivityRelationList) != contactsActivityRelationList.size()) {
                success = false;
            }
        }

        if (t != null) {
            t.setId(UUIDUtil.getUUID());
            t.setOwner(c.getOwner());
            t.setCustomerId(cus.getId());
            t.setSource(c.getSource());
            t.setContactsId(con.getId());
            t.setDescription(c.getDescription());
            t.setContactSummary(c.getContactSummary());
            t.setNextContactTime(c.getNextContactTime());
            t.setCreateTime(createTime);
            t.setCreateBy(createBy);

            if (tranDao.save(t) != 1) {
                success = false;
            }

            TranHistory th = new TranHistory();
            th.setId(UUIDUtil.getUUID());
            th.setStage(t.getStage());
            th.setMoney(t.getMoney());
            th.setExpectedDate(t.getExpectedDate());
            th.setCreateTime(createTime);
            th.setCreateBy(createBy);
            th.setTranId(t.getId());

            if (tranHistoryDao.save(th) != 1) {
                success = false;
            }
        }


        if (clueRemarkDao.delete(clueId) != clueRemarkList.size()) {
            success = false;
        }


        if (clueActivityRelationDao.delete(clueId) != clueActivityRelationList.size()) {
            success = false;
        }


        if (clueDao.delete(clueId) != 1) {
            success = false;
        }

        return success;
    }

}
