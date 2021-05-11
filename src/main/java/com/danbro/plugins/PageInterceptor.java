package com.danbro.plugins;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Danrbo
 * @Classname PageInterceptor
 * @Description TODO 分页拦截器
 * @Date 2021/5/11 13:09
 */
@Intercepts(@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}))
public class PageInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 1、判断是否有 page 参数
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        Object parameterObject = boundSql.getParameterObject();
        Page page = null;
        if (parameterObject instanceof Page) {
            page = (Page) parameterObject;
        } else if (parameterObject instanceof Map) {
            page = (Page) ((Map) parameterObject).values().stream().filter(e -> e instanceof Page).findFirst().orElse(null);
        }
        if (page == null) {
            return invocation.proceed();
        }
        // 2、查询总行数
        page.setTotal(selectCount(invocation));
        // 3、修改SQL语句实现分页的操作
        String newSql = String.format("%s limit %s offset %s", boundSql.getSql(), page.getSize(), page.getOffset());
        SystemMetaObject.forObject(boundSql).setValue("sql", newSql);
        return invocation.proceed();
    }

    private int selectCount(Invocation invocation) throws SQLException {
        int count = 0;
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        String countSql = String.format("select count(*) from (%s) as _page", statementHandler.getBoundSql().getSql());
        Connection connection = (Connection) invocation.getArgs()[0];
        PreparedStatement preparedStatement = connection.prepareStatement(countSql);
        // 把参数设置进去
        statementHandler.getParameterHandler().setParameters(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }
        resultSet.close();
        preparedStatement.close();
        return count;
    }
}
