package com.bjpowernode.crm.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionUtil {

    private SqlSessionUtil() {
    }

    private static SqlSessionFactory factory;
    private static ThreadLocal<SqlSession> local = new ThreadLocal<>();

    static{
        try {
            factory =
                    new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getSqlSession() {
        SqlSession sqlsession = local.get();
        if (sqlsession == null) {
            sqlsession = factory.openSession();
            local.set(sqlsession);
        }
        return sqlsession;
    }

    public static void close(SqlSession sqlsession) {
        if (sqlsession != null) {
            sqlsession.close();
            local.remove();
        }

    }
}
