/**
 *    Copyright 2009-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.executor.statement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.ResultHandler;

/**
 * @author Clinton Begin
 *
 * 复制与数据库的操作
 */
public interface StatementHandler {
  /**
   * 获取 Statement 对象，这个方法会通过 connection 创建 Statement 对象。
   */
  Statement prepare(Connection connection, Integer transactionTimeout)
      throws SQLException;

  /**
   *  对 Statement 设置参数，特别是 PreparedStatement 对象。
   */
  void parameterize(Statement statement)
      throws SQLException;

  /**
   * 批量处理 SQL
   */
  void batch(Statement statement)
      throws SQLException;

  /**
   *  执行更新
   */
  int update(Statement statement)
      throws SQLException;

  /**
   * 执行查询
   */
  <E> List<E> query(Statement statement, ResultHandler resultHandler)
      throws SQLException;

  /**
   *  查询返回游标对象
   */
  <E> Cursor<E> queryCursor(Statement statement)
      throws SQLException;

  /**
   * 获取动态的 SQL 语句
   */
  BoundSql getBoundSql();

  /**
   * 获取参数处理器
   */
  ParameterHandler getParameterHandler();

}
