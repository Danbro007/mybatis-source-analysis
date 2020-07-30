package com.danbro.mapper;

import com.danbro.pojo.Student;

import java.util.List;

/**
 * @Classname StudentMapper
 * @Description TODO
 * @Date 2020/7/29 20:16
 * @Author Danrbo
 */
public interface StudentMapper {

    List<Student> getStudents();
    List<Student> getStudents2();

}
