package com.danbro.pojo;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Danrbo
 * @Classname Blog
 * @Description TODO
 * @Date 2021/5/8 15:58
 */
@Data
@Accessors(chain = true)
public class Blog {
    private Integer id;
    private String name;
    private Author author;
    private List<Comment> commentList;

}
