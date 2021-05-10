package com.danbro.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Danrbo
 * @Classname Comment
 * @Description TODO
 * @Date 2021/5/8 15:58
 */
@Data
@Accessors(chain = true)
public class Comment {
    private Integer id;
    private String content;
    private Blog blog;
//    private Commenter commenter;


    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", blog=" + blog.getName() +
                '}';
    }
}
