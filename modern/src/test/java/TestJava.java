import kotlin.collections.CollectionsKt;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author young
 * @create 22/4/8 20:43
 */
public class TestJava {
    @Test
    public void testClass() {
        Object noname = new Object() {
            {
                System.out.println("hello, a noname object created");
            }

            public void m1() {
                System.out.println("m1");
            }

            @Override
            public String toString() {
                return "this is a noname object";
            }
        };
//        noname.m1();
        System.out.println(noname);
    }

    static <T> T[] toArray(List<T> list) {
        T[] array = (T[]) new Object[list.size()];
        return list.toArray(array);
    }

    @Test
    public void testArray() {
        toArray(CollectionsKt.mutableListOf(1, 2, 3));
        var list = new ArrayList<Integer>();
    }
}
