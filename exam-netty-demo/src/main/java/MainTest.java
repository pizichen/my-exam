import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @Title MainTest
 * @ProjectName exam
 * @Description 测试类
 * @Author Merlin Chen
 * @Date 2018/9/26 10:31
 **/
public class MainTest {

    public static void main(String[] args){

        Set<String> set = new HashSet<String>(10);
        set.add("1");
        set.add("2");
        set.add("3");
        set.add("4");
        set.add("5");
        Iterator<String> it = set.iterator();
        while (it.hasNext()){
            String str = it.next();
            // it.remove();
            System.out.println(str);
        }
        System.out.println("size: " + set.size());
    }
}
