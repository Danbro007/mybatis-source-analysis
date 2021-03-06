package com.danbro.mapper;

import java.util.List;

import com.danbro.plugins.Page;
import com.danbro.pojo.Author;
import com.danbro.pojo.Blog;
import com.danbro.pojo.Comment;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * @author Danrbo
 * @Classname BlogMapper
 * @Description TODO
 * @Date 2021/5/8 16:00
 */
@CacheNamespace
public interface BlogMapper {
    Blog selectBlogById(Integer id);

    Blog selectBlogById2(Integer id);

    Blog selectBlogById3(Blog blog);

    Author selectAuthorById(Integer id);

    List<Comment> selectCommentByBlogId(Integer id);

    List<Blog> selectBlogInIdList(List<Integer> list);

    List<Blog> selectListByPage(Page page);
}
