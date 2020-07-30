package com.danbro.mapper;

import com.danbro.pojo.Book;

import java.util.List;

/**
 * @Classname BookMapper
 * @Description TODO
 * @Date 2020/7/28 21:11
 * @Author Danrbo
 */
public interface BookMapper {
    List<Book> getBooks();
}
