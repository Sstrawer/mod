import org.junit.jupiter.api.Test;

/**
 * @author young
 * @create 22/4/8 20:43
 */
public class TestJava {
    @Test
    public void testClass() {
        Object noname = new Object(){
            {
                System.out.println("hello, a noname object created");
            }
            public void m1(){
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
}
