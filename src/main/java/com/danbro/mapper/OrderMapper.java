package com.danbro.mapper;

import java.util.List;
import com.danbro.pojo.Order;
import com.danbro.pojo.Person;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * @author Danrbo
 * @Classname BlogMapper
 * @Description TODO
 * @Date 2021/5/8 16:00
 */
@CacheNamespace
public interface OrderMapper {

    List<Order> selectOrderList();
    Person selectPersonById(Integer id);
}
