package sprouts.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;

public class FakeAnnotate implements AnnotatedElement {
    private List<Annotation> annotations;

    public FakeAnnotate() {
        this.annotations = new LinkedList<>();
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        if (annotationClass == null)
            throw new NullPointerException();

        for (Annotation annotation : annotations)
            if (annotation.annotationType().equals(annotationClass))
                return (T) annotation;

        return null;
    }

    @Override
    public Annotation[] getAnnotations() {
        return (Annotation[]) this.annotations.toArray();
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return (Annotation[]) this.annotations.toArray();
    }

    public FakeAnnotate addAnnotation(Annotation annotation) {
        this.annotations.add(annotation);
        return this;
    }
}
