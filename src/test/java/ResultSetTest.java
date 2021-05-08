import com.danbro.MybatisUtils;
import com.danbro.mapper.PeopleMapper;
import com.danbro.pojo.People;
import com.danbro.pojo.Person;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Danrbo
 * @Classname ResultSetTest
 * @Description TODO 结果集处理过程测试
 * @Date 2021/5/8 13:56
 */
public class ResultSetTest {

    @Test
    public void test01(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        List<People> peopleList = sqlSession.getMapper(PeopleMapper.class).selectListByAge(22);
        peopleList.forEach(System.out::println);
    }
    // 自定义 ResultHandler 的使用
    @Test
    public void test02(){
        List list = new ArrayList();
        ResultHandler resultHandler = resultContext -> {
            // 当查询的结果大于等于2则停止结果的封装
            if (resultContext.getResultCount() >= 2) {
                resultContext.stop();
            }
            list.add(resultContext.getResultObject());

        };
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        sqlSession.select("com.danbro.mapper.PeopleMapper.selectListAll",resultHandler);
        list.forEach(System.out::println);
    }

}
