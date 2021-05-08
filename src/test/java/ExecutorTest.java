import com.danbro.MybatisUtils;
import org.apache.ibatis.executor.BatchExecutor;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.ReuseExecutor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.transaction.jdbc.JdbcTransaction;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Danrbo
 * @Classname ExecutorTest
 * @Description TODO 测试各种不同的Executor
 * @Date 2021/5/7 13:36
 */
public class ExecutorTest {
    private SqlSession sqlSession;
    private JdbcTransaction jdbcTransaction;
    private Configuration configuration;

    @Before
    public void init() throws SQLException {
        sqlSession = MybatisUtils.getSqlSession();
        configuration = sqlSession.getConfiguration();
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mybatis?useSsl=true&amp;useUnicode=true&amp;characterEncoding=UTF-8",
                "root",
                "123456");
        jdbcTransaction = new JdbcTransaction(connection);
    }

    /**
     * 测试 SimpleExecutor
     * 缺点：每次执行都需要预编译，非常损耗性能。
     */
    @Test
    public void test1() throws SQLException {
        SimpleExecutor simpleExecutor = new SimpleExecutor(configuration, jdbcTransaction);
        MappedStatement ms = configuration.getMappedStatement("com.danbro.mapper.PersonMapper.getPersonById");
        List<Object> list1 = simpleExecutor.doQuery(ms, 1, RowBounds.DEFAULT, SimpleExecutor.NO_RESULT_HANDLER, ms.getBoundSql(1));
        System.out.println(list1.get(0));
        List<Object> list2 = simpleExecutor.doQuery(ms, 1, RowBounds.DEFAULT, SimpleExecutor.NO_RESULT_HANDLER, ms.getBoundSql(1));
        System.out.println(list2.get(0));
    }

    /**
     * 测试 ReuseExecutor
     * 优点：相同的sql语句只需要一次预编译，多次执行。
     */
    @Test
    public void test2() throws SQLException {
        ReuseExecutor reuseExecutor = new ReuseExecutor(configuration, jdbcTransaction);
        MappedStatement ms = configuration.getMappedStatement("com.danbro.mapper.PersonMapper.getPersonById");
        List<Object> list1 = reuseExecutor.doQuery(ms, 1, RowBounds.DEFAULT, ReuseExecutor.NO_RESULT_HANDLER, ms.getBoundSql(1));
        System.out.println(list1.get(0));
        List<Object> list2 = reuseExecutor.doQuery(ms, 1, RowBounds.DEFAULT, ReuseExecutor.NO_RESULT_HANDLER, ms.getBoundSql(1));
        System.out.println(list2.get(0));
    }

    /**
     * 测试 ReuseExecutor 批处理执行器
     * 只针对增删改操作，想要修改生效必须手动提交
     */
    @Test
    public void test3() throws SQLException {
        BatchExecutor batchExecutor = new BatchExecutor(configuration, jdbcTransaction);
        MappedStatement ms = configuration.getMappedStatement("com.danbro.mapper.PersonMapper.updatePersonById");
        Map map = new HashMap<>();
        map.put("arg0","danbro");
        map.put("arg1",1);
        batchExecutor.doUpdate(ms,map);
        batchExecutor.doUpdate(ms,map);
        batchExecutor.doUpdate(ms,map);
        batchExecutor.doUpdate(ms,map);
        // 手动提交
        batchExecutor.doFlushStatements(false);
    }

    /**
     * 测试 BaseExecutor ，只预编译了一次，这是因为执行的BaseExecutor的query方法，在query方法里会先查询缓存，缓存没有的话再执行子类的额doQuery方法
     * 。
     */
    @Test
    public void test4() throws SQLException {
        SimpleExecutor simpleExecutor = new SimpleExecutor(configuration, jdbcTransaction);
        MappedStatement ms = configuration.getMappedStatement("com.danbro.mapper.PersonMapper.getPersonById");
        List<Object> list1 = simpleExecutor.query(ms, 1, RowBounds.DEFAULT, SimpleExecutor.NO_RESULT_HANDLER);
        System.out.println(list1.get(0));
        // 到一级缓存查询
        List<Object> list2 = simpleExecutor.query(ms, 1, RowBounds.DEFAULT, SimpleExecutor.NO_RESULT_HANDLER);
        System.out.println(list2.get(0));
    }

    /**
     * 测试 CachingExecutor，CachingExecutor执行二级缓存的相关逻辑，Executor执行数据库的相关逻辑
     * 要把查询出的数据放入二级缓存则必须执行commit操作
     */
    @Test
    public void test5() throws SQLException {
        SimpleExecutor simpleExecutor = new SimpleExecutor(configuration, jdbcTransaction);
        // 装饰器模式，添加了执行器的二级缓存功能。
        CachingExecutor cachingExecutor = new CachingExecutor(simpleExecutor);
        MappedStatement ms = configuration.getMappedStatement("com.danbro.mapper.PersonMapper.getPersonById");
        List<Object> list1 = cachingExecutor.query(ms, 1, RowBounds.DEFAULT, SimpleExecutor.NO_RESULT_HANDLER);
        System.out.println(list1.get(0));
        cachingExecutor.commit(true);
        // 到二级缓存查询，二级缓存没有到一级缓存查找，一级缓存没有则到数据库查找
        List<Object> list2 = cachingExecutor.query(ms, 1, RowBounds.DEFAULT, SimpleExecutor.NO_RESULT_HANDLER);
        System.out.println(list2.get(0));
    }
}
