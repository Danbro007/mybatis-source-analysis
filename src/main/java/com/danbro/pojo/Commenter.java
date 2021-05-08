package com.danbro.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Danrbo
 * @Classname Commenter
 * @Description TODO
 * @Date 2021/5/8 15:59
 */
@Data
@Accessors(chain = true)
public class Commenter {
    private Integer id;
    private String name;
}
