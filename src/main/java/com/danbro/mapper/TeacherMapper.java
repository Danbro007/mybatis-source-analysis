package com.danbro.mapper;

import com.danbro.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Classname TeacherMapper
 * @Description TODO
 * @Date 2020/7/29 20:14
 * @Author Danrbo
 */
public interface TeacherMapper {

    @Select("select * from teacher where id = #{id}")
    Teacher getTeacherById(@Param("id") int id);

    Teacher getTeacherById2(@Param("id") int id);
}
