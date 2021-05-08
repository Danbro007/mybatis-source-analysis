package com.danbro.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Classname User
 * @Description TODO
 * @Date 2020/7/28 15:49
 * @Author Danrbo
 */
@Data
@Accessors(chain = true)
public class User implements Serializable {
    private int id;
    private String name;
    private String password;
    private Integer age;
    private int deleteFlag;
}
