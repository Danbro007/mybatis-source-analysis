import com.danbro.MybatisUtils;
import com.danbro.mapper.PersonMapper;
import com.danbro.mapper.UserMapper;
import com.danbro.pojo.Person;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

/**
 * @author Danrbo
 * @Classname SecondCacheTest
 * @Description TODO 二级缓存测试场景
 * @Date 2021/5/7 20:25
 */
public class SecondCacheTest {
    private SqlSession sqlSession;

    @Before
    public void init() throws SQLException {
        sqlSession = MybatisUtils.getSqlSession();
    }

    // 把数据放入二级缓存中
    @Test
    public void test01() {
        Configuration configuration = sqlSession.getConfiguration();
        Cache cache = configuration.getCache("com.danbro.mapper.PersonMapper");
        Person person = new Person().setName("Josh").setAge(12).setId(4);
        cache.putObject("Josh", person);
        cache.getObject("Josh");
    }

    // 在两个不同的SqlSession查询执行相同SQL语句，没有执行提交则不会命中二级缓存。
    @Test
    public void test02() {
        SqlSession sqlSession1 = MybatisUtils.getSqlSession();
        Person person1 = sqlSession1.getMapper(PersonMapper.class).getPersonById(1);
        SqlSession sqlSession2 = MybatisUtils.getSqlSession();
        Person person2 = sqlSession2.getMapper(PersonMapper.class).getPersonById(1);
        System.out.println(person1 == person2);
    }

    // 在两个不同的SqlSession查询执行相同SQL语句，要手动提交才会命中缓存（防止脏读）。
    @Test
    public void test03() {
        SqlSession sqlSession1 = MybatisUtils.getSqlSession();
        Person person1 = sqlSession1.getMapper(PersonMapper.class).getPersonById(1);
        sqlSession1.commit();
        SqlSession sqlSession2 = MybatisUtils.getSqlSession();
        Person person2 = sqlSession2.getMapper(PersonMapper.class).getPersonById(1);
    }

    // 在两个不同的SqlSession查询执行相同SQL语句，开启了自动提交不会命中缓存。
    @Test
    public void test04() {
        SqlSession sqlSession1 = MybatisUtils.getSqlSession(true);
        Person person1 = sqlSession1.getMapper(PersonMapper.class).getPersonById(1);
        SqlSession sqlSession2 = MybatisUtils.getSqlSession(true);
        Person person2 = sqlSession2.getMapper(PersonMapper.class).getPersonById(1);
    }

    // getPersonById方法开启 flushCache = true，就算是不同的方法的缓存也会被清空导致没有命中缓存。
    @Test
    public void test05() {
        SqlSession sqlSession1 = MybatisUtils.getSqlSession();
        sqlSession1.getMapper(PersonMapper.class).getPersonById3(1);
        // 这里由于getPersonById方法开启了flushCache=true，执行SQL后会清空整个缓存
        sqlSession1.getMapper(PersonMapper.class).getPersonById(1);
        // 提交了才能清空缓存
        sqlSession1.commit();

        SqlSession sqlSession2 = MybatisUtils.getSqlSession();
        sqlSession2.getMapper(PersonMapper.class).getPersonById3(1);
    }
    @Test
    public void test06() {
        SqlSession sqlSession1 = MybatisUtils.getSqlSession(true);
        sqlSession1.getMapper(PersonMapper.class).getPersonById(1);
        sqlSession1.getMapper(UserMapper.class).getUserById(1);
        System.out.println(sqlSession1);
    }
}
