/**
 *    Copyright 2009-2015 the original author or authors.
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
package org.apache.ibatis.mapping;

/**
 * @author Clinton Begin
 */
public enum StatementType {
  /**
   * 普通的不带参的查询SQL；支持批量更新,批量删除;
   */
  STATEMENT,
  /**
   * 可变参数的SQL,编译一次,执行多次,效率高;
   * 安全性好，有效防止Sql注入等问题;
   * 支持批量更新,批量删除;
   */
  PREPARED,
  /**
   * 继承自PreparedStatement,支持带参数的SQL操作;
   * 支持调用存储过程,提供了对输出和输入/输出参数(INOUT)的支持;
   */
  CALLABLE
}
