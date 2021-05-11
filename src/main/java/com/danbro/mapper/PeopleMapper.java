package com.danbro.mapper;

import com.danbro.plugins.Page;
import com.danbro.pojo.People;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Danrbo
 * @Classname PeopleMapper
 * @Description TODO
 * @Date 2021/5/8 12:08
 */
@CacheNamespace
public interface PeopleMapper {
    @Select("select * from people where id = #{id}")
    People selectOneById(Integer id);

    @Select("select * from people where id = #{arg0} and name = #{arg1}")
    People selectOneByNameAndId(Integer id, String name);

    @Select("select * from people where age = #{age}")
    List<People> selectListByAge(Integer age);

    @Select("select * from people")
    People selectListAll();

    @Select("select * from people")
    List<People> selectListByPage(Page page);

}
