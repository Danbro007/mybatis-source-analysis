package com.danbro.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Danrbo
 * @Classname Person
 * @Description TODO
 * @Date 2021/5/7 12:48
 */
@Data
@Accessors(chain = true)
public class Person implements Serializable {
    private String name;
    private Integer age;
    private Integer id;
}
