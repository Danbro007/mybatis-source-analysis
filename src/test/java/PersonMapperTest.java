import com.danbro.MybatisUtils;
import com.danbro.mapper.PersonMapper;
import com.danbro.pojo.Person;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.lang.invoke.MutableCallSite;
import java.util.List;

/**
 * @Classname BookMapperTest
 * @Description TODO
 * @Date 2020/7/28 21:14
 * @Author Danrbo
 */
public class PersonMapperTest {
    private Logger logger = Logger.getLogger(PersonMapperTest.class);

    @Test
    public void test1() {
        // 开启一级缓存，在当前session作用域下多次查询只会查询一次数据库
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        System.out.println(personMapper.getPersonById(1));
        System.out.println(personMapper.getPersonById(1));
        System.out.println(personMapper.getPersonById(1));
        sqlSession.close();
    }

    @Test
    public void test2() {
        // 开启一级缓存，在第二次查询前进行一次更新操作，之后的第二次查询会访问数据库
        // 即更新操作会造成一级缓存的失效
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        Person person = personMapper.getPersonById(1);
        System.out.println(person);
        System.out.println(personMapper.getPersonById(1));
        person.setName("danbro");
        personMapper.updatePersonById(person);
        System.out.println(personMapper.getPersonById(1));
        System.out.println(personMapper.getPersonById(1));
        sqlSession.close();
    }

    /**
     * 开启两个SqlSession，由于一级缓存只在同一session生效，所以sqlSession1先执行查询操作会到数据库查询，
     * sqlSession2 不会使用 sqlSession1 的缓存，而是自己去数据库查询
     */
    @Test
    public void test3() {
        SqlSession sqlSession1 = MybatisUtils.getSqlSession(true);
        SqlSession sqlSession2 = MybatisUtils.getSqlSession(true);

        PersonMapper personMapper1 = sqlSession1.getMapper(PersonMapper.class);
        PersonMapper personMapper2 = sqlSession2.getMapper(PersonMapper.class);

        System.out.println("personMapper1读取数据: " + personMapper1.getPersonById(1));
        System.out.println("personMapper2读取数据: " + personMapper2.getPersonById(1));
        Person person = personMapper1.getPersonById(1);
        person.setName("Bush");
        personMapper2.updatePersonById(person);
        System.out.println("personMapper2更新了");
        // personMapper1 还是使用的缓存数据
        System.out.println("personMapper1读取数据: " + personMapper1.getPersonById(1));
        // personMapper2 会重新访问数据库
        System.out.println("personMapper2读取数据: " + personMapper2.getPersonById(1));
    }

    /**
     * 测试添加一条记录后返会记录的ID
     */
    @Test
    public void test04(){
        SqlSession sqlSession = MybatisUtils.getSqlSession(true);
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        Person person = new Person().setName("Jack").setAge(32);
        personMapper.insertPerson(person);
        System.out.println(person.getId());
    }

    @Test
    public void test05(){
        SqlSession sqlSession = MybatisUtils.getSqlSession(true);
        PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
        List<Person> personList = personMapper.selectListByCondition("Jack", 32);
        personList.forEach(System.out::println);
    }
}
