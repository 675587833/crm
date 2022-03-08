package com.bjpowernode.crm.util;

public class ServiceFactory {

    private ServiceFactory() {
    }

    public static Object getService(Object serviceImpl){
        return new TransactionInvocationHandle(serviceImpl).getProxy();
    }
}
