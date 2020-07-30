import com.danbro.MybatisUtils;
import com.danbro.mapper.StudentMapper;
import com.danbro.pojo.Student;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @Classname StudentMapperTest
 * @Description TODO
 * @Date 2020/7/29 20:38
 * @Author Danrbo
 */
public class StudentMapperTest {
    @Test
    public void getStudents(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        List<Student> students = sqlSession.getMapper(StudentMapper.class).getStudents();
        students.forEach(System.out::println);
    }

    @Test
    public void getStudents2(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        List<Student> students = sqlSession.getMapper(StudentMapper.class).getStudents2();
        students.forEach(System.out::println);
    }
}
