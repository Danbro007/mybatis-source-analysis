package com.danbro.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Classname Book
 * @Description TODO
 * @Date 2020/7/28 21:07
 * @Author Danrbo
 */
@Data
public class Book {
    private int id;
    private String name;
    private BigDecimal price;
}
