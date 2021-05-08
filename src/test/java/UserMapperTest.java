import com.danbro.MybatisUtils;
import com.danbro.mapper.UserMapper;
import com.danbro.pojo.Teacher;
import com.danbro.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname UserMapperTest
 * @Description TODO
 * @Date 2020/7/28 15:58
 * @Author Danrbo
 */
public class UserMapperTest {
    @Test
    public void getUsers(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        List<User> users = sqlSession.getMapper(UserMapper.class).getUsers();
        users.forEach(System.out::println);
        sqlSession.close();
    }

    @Test
    public void getUserById(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        User user = sqlSession.getMapper(UserMapper.class).getUserById(1);
        System.out.println(user);
        sqlSession.close();
    }

    @Test
    public void insertUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        User user = new User();
        user.setId(2);
        user.setName("Marry");
        user.setPassword("123");
        user.setAge(22);
        user.setDeleteFlag(0);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        int i = userMapper.insertUser(user);
        System.out.println(i);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void updateUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        User user = new User();
        user.setId(1);
        user.setName("JACK");
        user.setPassword("123456");
        sqlSession.getMapper(UserMapper.class).updateUser(user);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void deleteUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        sqlSession.getMapper(UserMapper.class).deleteUser(1);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void getUsersByLikeName(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        List<User> users = sqlSession.getMapper(UserMapper.class).getUsersByLikeName("李");
        users.forEach(System.out::println);
        sqlSession.close();
    }

    @Test
    public void updateUserById(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = new User().setId(1).setName("Helen");
        userMapper.updateUserById(user);
        sqlSession.commit();
        sqlSession.close();
    }

    /**
     * 测试 foreach标签的使用
     */
    @Test
    public void getTeacherListInIdList(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        List<Teacher> teacherList = userMapper.getTeacherListInIdList(list);
        teacherList.forEach(System.out::println);
        sqlSession.close();
    }
}
