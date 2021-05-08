/**
 *    Copyright 2009-2019 the original author or authors.
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
package org.apache.ibatis.executor;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cache.TransactionalCacheManager;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

/**
 * @author Clinton Begin
 * @author Eduardo Macarron
 *
 * 增强执行器的二级缓存功能
 *
 */
public class CachingExecutor implements Executor {
  // 要增强的执行器
  private final Executor delegate;
  // 事务缓存管理器，可以管理多个缓存空间
  private final TransactionalCacheManager tcm = new TransactionalCacheManager();

  public CachingExecutor(Executor delegate) {
    this.delegate = delegate;
    delegate.setExecutorWrapper(this);
  }

  @Override
  public Transaction getTransaction() {
    return delegate.getTransaction();
  }

  @Override
  public void close(boolean forceRollback) {
    try {
      //issues #499, #524 and #573
      if (forceRollback) {
        tcm.rollback();
      } else {
        tcm.commit();
      }
    } finally {
      delegate.close(forceRollback);
    }
  }

  @Override
  public boolean isClosed() {
    return delegate.isClosed();
  }

  @Override
  public int update(MappedStatement ms, Object parameterObject) throws SQLException {
    // 清空暂存区的数据
    flushCacheIfRequired(ms);
    return delegate.update(ms, parameterObject);
  }

  @Override
  public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
    // 获取动态 SQL
    BoundSql boundSql = ms.getBoundSql(parameterObject);
    // 创建一个缓存对象 CacheKey，它里面缓存了 ms 的id、SQL语句、参数等。
    CacheKey key = createCacheKey(ms, parameterObject, rowBounds, boundSql);
    // 执行查询语句并返回结果
    return query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
  }

  @Override
  public <E> Cursor<E> queryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException {
    flushCacheIfRequired(ms);
    return delegate.queryCursor(ms, parameter, rowBounds);
  }

  /**
   * 先到缓存获取查询的结果，如果没有则让SimpleExecutor到数据库查。
   * 这里的缓存是二级缓存，二级缓存是可以被其他SqlSession共享的
   */
  @Override
  public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql)
      throws SQLException {
    // 尝试获取缓存对象
    Cache cache = ms.getCache();
    if (cache != null) {
      // （二级缓存失效场景1）是否开了每次查询前都清空暂存区的功能（查询的时候flushCache默认是false的,update时默认是true）
      flushCacheIfRequired(ms);
      // 如果开启了使用二级缓存并且没有结果处理器
      if (ms.isUseCache() && resultHandler == null) {
        // 对 SQL 语句进行检查
        ensureNoOutParams(ms, boundSql);
        @SuppressWarnings("unchecked")
        // 到缓存暂存区获取缓存，缓存暂存区再到二级缓存查找
        List<E> list = (List<E>) tcm.getObject(cache, key);
        if (list == null) {
          // 二级缓存没有则到一级缓存查找（BaseExecutor）
          list = delegate.query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
          // 把查询结果放到缓存暂存区内（此时数据还没有存到二级缓存中）
          tcm.putObject(cache, key, list); // issue #578 and #116
        }
        return list;
      }
    }
    // 缓存没有开启则让真正干活的SimpleExecutor执行器来执行查询
    return delegate.query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
  }

  @Override
  public List<BatchResult> flushStatements() throws SQLException {
    return delegate.flushStatements();
  }

  @Override
  public void commit(boolean required) throws SQLException {
    // 执行BaseExecutor的commit方法
    delegate.commit(required);
    // 把事务缓存管理器的缓存提交到二级缓存
    tcm.commit();
  }

  @Override
  public void rollback(boolean required) throws SQLException {
    try {
      delegate.rollback(required);
    } finally {
      if (required) {
        tcm.rollback();
      }
    }
  }
  // 确保没有输出的参数
  private void ensureNoOutParams(MappedStatement ms, BoundSql boundSql) {
    // 如果当前的 Statement 是 CALLABLE 类型既支持存储过程的 Statement
    if (ms.getStatementType() == StatementType.CALLABLE) {
      for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
        if (parameterMapping.getMode() != ParameterMode.IN) {
          throw new ExecutorException("Caching stored procedures with OUT params is not supported.  Please configure useCache=false in " + ms.getId() + " statement.");
        }
      }
    }
  }

  @Override
  public CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql) {
    return delegate.createCacheKey(ms, parameterObject, rowBounds, boundSql);
  }

  @Override
  public boolean isCached(MappedStatement ms, CacheKey key) {
    return delegate.isCached(ms, key);
  }

  @Override
  public void deferLoad(MappedStatement ms, MetaObject resultObject, String property, CacheKey key, Class<?> targetType) {
    delegate.deferLoad(ms, resultObject, property, key, targetType);
  }

  @Override
  public void clearLocalCache() {
    delegate.clearLocalCache();
  }

  private void flushCacheIfRequired(MappedStatement ms) {
    // 尝试获取缓存，如果有缓存并且设置了清除缓存则把缓存清除
    Cache cache = ms.getCache();
    // 如果缓存存在并且开启了清除缓存
    if (cache != null && ms.isFlushCacheRequired()) {
      // 清理暂存区的数据
      tcm.clear(cache);
    }
  }

  @Override
  public void setExecutorWrapper(Executor executor) {
    throw new UnsupportedOperationException("This method should not be called");
  }

}
