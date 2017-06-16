import sprouts.Sprouts;
import sprouts.annotation.GetInstance;
import sprouts.settings.SproutsSettings;
import sprouts.support.FakeAnnotate;

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
        Sprouts sprouts = new Sprouts(new SproutsSettings() {
            @Override
            public void configure() {
                bind(AnnotatedElement.class).to(FakeAnnotate.class);
                allowPrivateMethod = true;
            }
        });
        TestMain test = (TestMain) sprouts.getNewInstance(TestMain.class);
        test.entry();
    }

    public void entry() {
        System.out.print(annotate.toString());
    }

    @GetInstance
    public void injectMe(AnnotatedElement element) {
        this.annotate = element;
    }
}
