import sprouts.Sprouts;
import sprouts.annotation.BindWith;
import sprouts.annotation.GetInstance;

import java.lang.reflect.AnnotatedElement;

public class TestMain {
    String lol;
    AnnotatedElement annotate;

    @GetInstance
    public TestMain(String lol) {
        this.lol = lol;
    }

    public static void main(String[] args) {
        System.out.print("ciao static main\n");
        Sprouts sprouts = new Sprouts(null);
        TestMain test = (TestMain) sprouts.getNewInstance(TestMain.class);
        test.entry();
    }

    public void entry() {
        System.out.print(annotate.toString());
    }

    @GetInstance
    private void injectMe(@BindWith(className = "sprouts.support.FakeAnnotate") AnnotatedElement element) {
        this.annotate = element;
    }
}
