package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.setting.domain.DicValue;
import com.bjpowernode.crm.setting.service.DicService;
import com.bjpowernode.crm.setting.service.impl.DicServiceImpl;
import com.bjpowernode.crm.util.ServiceFactory;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.*;

@WebListener
public class SysInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {

        ServletContext application = event.getServletContext();
        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map =  ds.getDicValue();
        Set<String> codes = map.keySet();
        for (String code : codes) {
            List<DicValue> dicValues = map.get(code);
            application.setAttribute(code,dicValues);
        }

        ResourceBundle bundle = ResourceBundle.getBundle("stage2possibility");
        Map<String,String> map2 = new HashMap<>();
        Enumeration<String> keys = bundle.getKeys();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            String value = bundle.getString(key);
            map2.put(key,value);
        }
        application.setAttribute("sp",map2);


    }
}
