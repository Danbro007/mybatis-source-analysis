import com.danbro.MybatisUtils;
import com.danbro.mapper.BlogMapper;
import com.danbro.pojo.*;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Danrbo
 * @Classname OgnlTest
 * @Description TODO OGNL表达式测试
 * @Date 2021/5/10 14:27
 */
public class OGNLTest {
    @Test
    public void test01(){
        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
        Comment comment1 = new Comment().setId(1).setContent("非常好");
        Comment comment2 = new Comment().setId(2).setContent("很一般");
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);
        Blog blog = new Blog().setId(1).setName("Java进阶").setAuthor(new Author().setId(1).setName("Hellen")).setCommentList(comments);
        // 访问属性
        System.out.println(expressionEvaluator.evaluateBoolean("id!=null || author.name!=null",blog));
        // 调用方法
        System.out.println(expressionEvaluator.evaluateBoolean("commentList!=null && commentList.size>0",blog));
        System.out.println(expressionEvaluator.evaluateBoolean("commentList!=null && commentList.get(0).content!=null",blog));
        System.out.println(expressionEvaluator.evaluateBoolean("commentList!=null && commentList[0].content!=null",blog));
        // 遍历集合
        Iterable<?> commentList = expressionEvaluator.evaluateIterable("commentList", blog);
        commentList.forEach(System.out::println);
    }

    /**
     * <if> 标签和 <where> 执行流程分析
     */
    @Test
    public void test02(){
        Configuration configuration = MybatisUtils.getSqlSession().getConfiguration();
        Person person = new Person().setId(1).setName("Danbro").setAge(30);
        DynamicContext context = new DynamicContext(configuration, person);
        // 静态逻辑节点
        new StaticTextSqlNode("select * from users where 1 = 1").apply(context);
        // 执行IF逻辑
        IfSqlNode ifSqlNode = new IfSqlNode(new StaticTextSqlNode("and id = #{id}"), "id != null");
        ifSqlNode.apply(context);
        System.out.println(context.getSql());
    }

    @Test
    public void test03(){
        Configuration configuration = MybatisUtils.getSqlSession().getConfiguration();
        Person person = new Person().setName("Danbro").setAge(30);
        DynamicContext context = new DynamicContext(configuration, person);
        // 静态逻辑节点
        new StaticTextSqlNode("select * from users where 1 = 1").apply(context);
        // 执行IF逻辑
        IfSqlNode ifSqlNode = new IfSqlNode(new StaticTextSqlNode("and id = #{id}"), "id != null");
        ifSqlNode.apply(context);
        System.out.println(context.getSql());
    }

    /**
     * 不用 <where> 标签出现的问题
     */
    @Test
    public void test04(){
        Configuration configuration = MybatisUtils.getSqlSession().getConfiguration();
        Person person = new Person().setId(1).setName("Danbro").setAge(30);
        DynamicContext context = new DynamicContext(configuration, person);
        // 静态逻辑节点
        new StaticTextSqlNode("select * from users where").apply(context);
        // 执行IF逻辑
        IfSqlNode ifSqlNode = new IfSqlNode(new StaticTextSqlNode("and id = #{id}"), "id != null");
        ifSqlNode.apply(context);
        System.out.println(context.getSql());
    }

    /**
     * 使用 <where> 解决 test04 的问题
     */
    @Test
    public void test06(){
        Configuration configuration = MybatisUtils.getSqlSession().getConfiguration();
        Person person = new Person().setId(1).setName("Danbro").setAge(30);
        DynamicContext context = new DynamicContext(configuration, person);
        new StaticTextSqlNode("select * from users").apply(context);
        // 执行IF逻辑
        IfSqlNode ifSqlNode = new IfSqlNode(new StaticTextSqlNode("and id = #{id}"), "id != null");
        // 执行Where逻辑
        // 1、添加 Where 前缀
        // 2、删除指定的关键字的前后缀，比如 and、or等
        WhereSqlNode whereSqlNode = new WhereSqlNode(configuration, ifSqlNode);
        whereSqlNode.apply(context);
        System.out.println(context.getSql());
    }


    /**
     *  <where> 标签下有多个 <if> 标签
     */
    @Test
    public void test07(){
        Configuration configuration = MybatisUtils.getSqlSession().getConfiguration();
        Person person = new Person().setId(1).setName("Danbro").setAge(30);
        DynamicContext context = new DynamicContext(configuration, person);
        new StaticTextSqlNode("select * from users").apply(context);
        // 多个If条件放入MixedSqlNode里
        IfSqlNode ifSqlNode1 = new IfSqlNode(new StaticTextSqlNode("and id = #{id}"), "id != null");
        IfSqlNode ifSqlNode2 = new IfSqlNode(new StaticTextSqlNode("and name = #{name}"), "name != null");
        List<SqlNode> list = new ArrayList<>();
        list.add(ifSqlNode1);
        list.add(ifSqlNode2);
        MixedSqlNode mixedSqlNode = new MixedSqlNode(list);
        // 执行Where逻辑
        // 1、添加 Where 前缀
        // 2、删除指定的关键字的前后缀
        WhereSqlNode whereSqlNode = new WhereSqlNode(configuration, mixedSqlNode);
        whereSqlNode.apply(context);
        System.out.println(context.getSql());
    }

    /**
     * 测试 <if> 和 <where> 标签的解析流程
     */
    @Test
    public void test08(){
        Configuration configuration = MybatisUtils.getSqlSession().getConfiguration();
        Person person = new Person().setId(1).setName("Danbro").setAge(30);
        DynamicContext context = new DynamicContext(configuration, person);
        new StaticTextSqlNode("select * from users").apply(context);
        // 多个If条件放入MixedSqlNode里
        IfSqlNode ifSqlNode1 = new IfSqlNode(new StaticTextSqlNode("and id = #{id}"), "id != null");
        IfSqlNode ifSqlNode2 = new IfSqlNode(new StaticTextSqlNode("and name = #{name}"), "name != null");
        List<SqlNode> list = new ArrayList<>();
        list.add(ifSqlNode1);
        list.add(ifSqlNode2);
        MixedSqlNode mixedSqlNode = new MixedSqlNode(list);
        // 执行Where逻辑
        // 1、添加 Where 前缀
        // 2、删除指定的关键字的前后缀
        WhereSqlNode whereSqlNode = new WhereSqlNode(configuration, mixedSqlNode);
        whereSqlNode.apply(context);
        System.out.println(context.getSql());
    }

    /**
     * 测试<foreach>标签的执行过程分析
     */
    @Test
    public void test09(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Blog> blogList = blogMapper.selectBlogInIdList(list);
        blogList.forEach(System.out::println);
    }

    /**
     * 分析把xml文件里动态SQL解析为MixedSqlNode
     */
    @Test
    public void test10(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
        Blog blog = blogMapper.selectBlogById3(new Blog().setId(1).setName("java进阶"));
        System.out.println(blog);
    }
}
