package com.bjpowernode.crm.util;

import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TransactionInvocationHandle implements InvocationHandler {

    private Object target;

    public TransactionInvocationHandle(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        SqlSession sqlSession = null;
        Object obj = null;
        try {
            sqlSession = SqlSessionUtil.getSqlSession();
            obj = method.invoke(target, args);
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            e.printStackTrace();
            throw e.getCause();
        } finally {
            SqlSessionUtil.close(sqlSession);
        }
        return obj;
    }


    public Object getProxy() {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }
}
