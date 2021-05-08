package com.danbro.pojo;

import lombok.Data;

import java.util.List;

/**
 * @Classname Teacher
 * @Description TODO
 * @Date 2020/7/29 20:15
 * @Author Danrbo
 */
@Data
public class Teacher {
    private Integer id;
    private String name;
    List<Student> students;
}
