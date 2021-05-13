package com.danbro.mapper;


import com.danbro.pojo.Person;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.CacheNamespaceRef;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.cache.Cache;

import java.util.List;

/**
 * @author Danrbo
 * @Classname PersonMapper
 * @Description TODO
 * @Date 2021/5/6 15:34
 */
//@CacheNamespace
public interface PersonMapper {
    //    @Options(useCache = false)// 关闭缓存
//    @Options(flushCache = Options.FlushCachePolicy.TRUE)// 清空整个缓存
    Person getPersonById(Integer id);

    Person getPersonById2(Integer id);

    Person getPersonById3(Integer id);

    void updatePersonById(Person person);

    void updatePersonNameById(String name, Integer id);

    void insertPerson(Person person);

    List<Person> selectListByCondition(String name,Integer age);
}
