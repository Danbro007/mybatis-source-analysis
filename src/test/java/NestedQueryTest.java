import com.danbro.MybatisUtils;
import com.danbro.mapper.BlogMapper;
import com.danbro.pojo.Blog;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * @Classname NestedQueryTest
 * @Description TODO
 * @Date 2021/5/8 21:49
 * @Created by Administrator
 */
public class NestedQueryTest {

    /**
     * 测试用延迟加载解决循环引用问题
     */
    @Test
    public void test01(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
        Blog blog = blogMapper.selectBlogById(1);
        System.out.println(blog);
    }
}
