package com.danbro.pojo;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @Classname Author
 * @Description TODO
 * @Date 2021/5/8 21:41
 * @Created by Administrator
 */
@Data
@Accessors(chain = true)
public class Author {
    private Integer id;
    private String name;
}
