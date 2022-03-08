package com.bjpowernode.crm.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
    private DateTimeUtil() {
    }

    public static String getSysTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
