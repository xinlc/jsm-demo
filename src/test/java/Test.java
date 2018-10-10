import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.function.Consumer;

public class Test {
    public static void main(String[] args) {

//        Timer t = new Timer(1000, System.out::println);
//        Timer t = new Timer(1000, event -> System.out.println(event));

        new Test().t(() -> System.out.println(1));
        new Test().t2((String t) -> System.out.println(t));


        new Test().new TT();
    }

    public void t(Runnable action) {
        action.run();
        this.new TT();
    }
    public void t2(Consumer<String> t) {
        t.accept("2");
    }

    public class TT {
        public void print() {
            System.out.println(111);
        }
    }
}

