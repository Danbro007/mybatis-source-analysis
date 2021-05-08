import com.danbro.pojo.Blog;
import com.danbro.pojo.Comment;
import com.danbro.pojo.Commenter;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author Danrbo
 * @Classname MetaObjectTest
 * @Description TODO
 * @Date 2021/5/8 16:02
 */
public class MetaObjectTest {
    @Test
    public void test01(){
        Commenter commenter = new Commenter().setId(1).setName("Jack");
        Comment comment = new Comment().setCommenter(commenter).setContent("hello").setId(1);
        ArrayList<Comment> list = new ArrayList<>();
        list.add(comment);
        Blog blog = new Blog().setName("Blog1").setId(1).setCommentList(list);

        Configuration configuration = new Configuration();
        MetaObject metaObject = configuration.newMetaObject(blog);
        Object value = metaObject.getValue("commentList[0].commenter.name");
        System.out.println(value);
    }
}
