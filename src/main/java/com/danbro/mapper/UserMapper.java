package com.danbro.mapper;

import com.danbro.pojo.Teacher;
import com.danbro.pojo.User;
import org.apache.ibatis.annotations.CacheNamespace;

import java.util.List;

/**
 * @Classname UserMappper
 * @Description TODO
 * @Date 2020/7/28 15:51
 * @Author Danrbo
 */
public interface UserMapper {
    /**
     * 获取所有用户
     *
     * @return 所有用户
     */
    List<User> getUsers();

    /**
     * 通过用户 id 获取用户信息
     *
     * @param id 用户 id
     * @return 用户信息
     */
    User getUserById(int id);

    /**
     * 添加用户
     *
     * @param user 要添加的用户信息
     */
    int insertUser(User user);

    /**
     * 更新用户信息
     *
     * @param user 要更新的用户信息
     */
    void updateUser(User user);

    /**
     * 通过用户 id 删除用户
     *
     * @param id 用户id
     */
    void deleteUser(int id);

    /**
     * 模糊匹配
     *
     * @param name 要模糊匹配的用户名
     * @return 用户列表
     */
    List<User> getUsersByLikeName(String name);

    User findUserById(int id);

    void updateUserById(User user);

    List<Teacher> getTeacherListInIdList(List<Integer> list);
}
