import java.util.List;
import com.danbro.MybatisUtils;
import com.danbro.mapper.OrderMapper;
import com.danbro.pojo.Order;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * @Classname LazyTest
 * @Description TODO 懒加载测试
 * @Date 2021/5/9 12:26
 * @Created by Administrator
 */
public class LazyTest {
    /**
     * 懒加载测试
     */
    @Test
    public void test01(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
        // 在这里只会执行查询order的SQL
        List<Order> orders = orderMapper.selectOrderList();
        // 这里会执行查询order里person的SQL语句
        orders.forEach(System.out::println);
    }
}
