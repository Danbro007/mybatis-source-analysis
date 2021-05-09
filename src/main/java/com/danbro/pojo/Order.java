package com.danbro.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Classname Order
 * @Description TODO
 * @Date 2021/5/9 12:48
 * @Created by Administrator
 */
@Data
@Accessors(chain = true)
public class Order {
    private Integer id;
    private String title;
    private Person person;
}
