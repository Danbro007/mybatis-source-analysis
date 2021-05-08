import com.danbro.MybatisUtils;
import com.danbro.mapper.PeopleMapper;
import com.danbro.pojo.People;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * @author Danrbo
 * @Classname ParamResolverTest
 * @Description TODO 测试参数解析器
 * @Date 2021/5/8 12:11
 */
public class ParamResolverTest {
    /**
     * 测试解析一个参数
     */
    @Test
    public void test01(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        People people = sqlSession.getMapper(PeopleMapper.class).selectOneById(1);
    }

    /**
     * 解析两个参数
     */
    @Test
    public void test02(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        People people = sqlSession.getMapper(PeopleMapper.class).selectOneByNameAndId(1,"danbro");
    }
}
