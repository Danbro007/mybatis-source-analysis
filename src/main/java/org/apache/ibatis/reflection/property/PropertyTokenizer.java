/**
 *    Copyright 2009-2017 the original author or authors.
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
package org.apache.ibatis.reflection.property;

import java.util.Iterator;

/**
 * @author Clinton Begin
 *
 * 属性分词器
 *
 */
public class PropertyTokenizer implements Iterator<PropertyTokenizer> {
  // 集合名
  private String name;
  // 集合名[索引]
  private final String indexedName;
  // 获取集合下标索引
  private String index;
  // 子属性字符串
  private final String children;

  public PropertyTokenizer(String fullname) {
    // 比如 commentList[0].commenter.name
    int delim = fullname.indexOf('.');
    if (delim > -1) {
      // 截取第一个 . 之前的字符串即 commentList[0]
      name = fullname.substring(0, delim);
      // 截取第一个 . 后面的字符串 commenter.name
      children = fullname.substring(delim + 1);
    } else {
      name = fullname;
      children = null;
    }
    indexedName = name;
    delim = name.indexOf('[');
    if (delim > -1) {
      // 获取集合下标索引
      index = name.substring(delim + 1, name.length() - 1);
      // 获取集合名
      name = name.substring(0, delim);
    }
  }

  public String getName() {
    return name;
  }

  public String getIndex() {
    return index;
  }

  public String getIndexedName() {
    return indexedName;
  }

  public String getChildren() {
    return children;
  }

  @Override
  public boolean hasNext() {
    return children != null;
  }

  @Override
  public PropertyTokenizer next() {
    return new PropertyTokenizer(children);
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("Remove is not supported, as it has no meaning in the context of properties.");
  }
}
