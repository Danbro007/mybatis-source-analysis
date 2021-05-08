import com.danbro.MybatisUtils;
import com.danbro.mapper.TeacherMapper;
import com.danbro.pojo.Teacher;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * @Classname TeacherMapperTest
 * @Description TODO
 * @Date 2020/7/29 20:24
 * @Author Danrbo
 */
public class TeacherMapperTest {
    @Test
    public void getTeacherById(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        Teacher teacher = sqlSession.getMapper(TeacherMapper.class).getTeacherById(1);
        System.out.println(teacher);
    }

    @Test
    public void getTeacherById2(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        // 执行代理对象的getTeacherById2()方法
        Teacher teacher = sqlSession.getMapper(TeacherMapper.class).getTeacherById2(1);
        System.out.println(teacher);
    }
}
