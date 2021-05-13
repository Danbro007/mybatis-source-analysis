import com.danbro.MybatisUtils;
import com.danbro.mapper.BookMapper;
import com.danbro.pojo.Book;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.List;

/**
 * @Classname BookMapperTest
 * @Description TODO
 * @Date 2020/7/28 21:14
 * @Author Danrbo
 */
public class BookMapperTest {
    private Logger logger = Logger.getLogger(BookMapperTest.class);
    @Test
    public void test1() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        List<Book> books = sqlSession.getMapper(BookMapper.class).getBooks();
        books.forEach(System.out::println);
        sqlSession.close();
    }

    /**
     * 测试开启二级缓存
     * @throws InterruptedException
     */
    @Test
    public void test2() throws InterruptedException {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        SqlSession sqlSession2 = MybatisUtils.getSqlSession();
        logger.debug("------------第一次-------------");
        List<Book> books = sqlSession.getMapper(BookMapper.class).getBooks();
        books.forEach(System.out::println);
        sqlSession.close();
        logger.debug("------------第二次-------------");
        List<Book> books1 = sqlSession2.getMapper(BookMapper.class).getBooks();
        books1.forEach(System.out::println);
        sqlSession2.close();
    }
}
