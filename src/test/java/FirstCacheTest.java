import com.danbro.MybatisUtils;
import com.danbro.mapper.PersonMapper;
import com.danbro.pojo.Person;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Danrbo
 * @Classname FirstCacheTest
 * @Description TODO 一级缓存测试场景
 * @Date 2021/5/7 16:03
 */
public class FirstCacheTest {

    private SqlSession sqlSession;

    @Before
    public void init() throws SQLException {
        sqlSession = MybatisUtils.getSqlSession();
    }

    // 同一SqlSession下查询能使用到一级缓存
    @Test
    public void test01() {
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        Person person = personMapper.getPersonById(1);
        Person person2 = personMapper.getPersonById(1);
        System.out.println(person == person2);
    }

    // 同一SqlSession下相同的SQL但是不相同的方法不能用到一级缓存
    @Test
    public void test02() {
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        Person person = personMapper.getPersonById(1);
        Person person2 = personMapper.getPersonById2(1);
        System.out.println(person == person2);
    }

    // 不同SqlSession下相同的查询不能命中缓存
    @Test
    public void test03() {
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        Person person = personMapper.getPersonById(1);
        Person person2 = MybatisUtils.getSqlSession().getMapper(PersonMapper.class).getPersonById(1);
        System.out.println(person == person2);
    }

    // 在同一SqlSession下，进行了自定义的分页则不会命中缓存
    @Test
    public void test04() {
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        Person person = personMapper.getPersonById(1);
        RowBounds rowBounds = new RowBounds(0, 10);
        List<Object> list = sqlSession.selectList("com.danbro.mapper.PersonMapper.getPersonById", 1, rowBounds);
        System.out.println(person == list.get(0));
    }

    // 在同一SqlSession下，使用默认的分页则命中缓存
    @Test
    public void test05() {
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        Person person = personMapper.getPersonById(1);
        List<Object> list = sqlSession.selectList("com.danbro.mapper.PersonMapper.getPersonById",
                1,
                RowBounds.DEFAULT);
        System.out.println(person == list.get(0));
    }

    // 同一SqlSession下手动清空缓存则不会命中缓存
    @Test
    public void test06() {
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        Person person = personMapper.getPersonById(1);
        sqlSession.clearCache();
        Person person2 = personMapper.getPersonById(1);
        System.out.println(person == person2);
    }



    // 同一SqlSession下在Mapper上开启了 @Options(flushCache = Options.FlushCachePolicy.TRUE) 注解
    // 或者 在Mapper.xml方法的标签添加了flushCache="true" 则每次执行后会清空一级缓存
    @Test
    public void test07() {
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        Person person = personMapper.getPersonById2(1);
        Person person2 = personMapper.getPersonById2(1);
        System.out.println(person == person2);
    }
    // 在同一SqlSession下，执行第二次查询前进行了更新操作则第二次查询不会命中缓存（防止出现数据一致性问题）
    // 即使修改的数据不是要查询的，也会清空缓存
    @Test
    public void test08() {
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        Person person = personMapper.getPersonById(1);
        person.setName("hello");
        personMapper.updatePersonById(person);
        Person person2 = personMapper.getPersonById(1);
        System.out.println(person == person2);
    }
    // 同一SqlSession下，如果作用域设置为STATEMENT,则不会命中缓存
    // 注意：如果是在一个嵌套查询还是能命中缓存的，STATEMENT并不是关闭一级缓存，而是把作用域缩小了。
    @Test
    public void test09() {
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        Person person = personMapper.getPersonById(1);
        Person person2 = personMapper.getPersonById(1);
        System.out.println(person == person2);
    }

}
