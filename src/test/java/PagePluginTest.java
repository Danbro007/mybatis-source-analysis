import com.danbro.MybatisUtils;
import com.danbro.mapper.BlogMapper;
import com.danbro.mapper.PeopleMapper;
import com.danbro.mapper.PersonMapper;
import com.danbro.plugins.Page;
import com.danbro.pojo.Blog;
import com.danbro.pojo.People;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @author Danrbo
 * @Classname PagePluginTest
 * @Description TODO 自己编写的分页插件测试
 * @Date 2021/5/11 13:26
 */
public class PagePluginTest {
    @Test
    public void test01(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        PeopleMapper peopleMapper = sqlSession.getMapper(PeopleMapper.class);
        Page page = new Page().setIndex(1).setSize(2);
        List<People> peopleList = peopleMapper.selectListByPage(page);
        System.out.println("总行数：" +page.getTotal());
        peopleList.forEach(System.out::println);
    }
}
