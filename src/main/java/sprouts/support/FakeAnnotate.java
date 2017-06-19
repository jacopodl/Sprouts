/*
    MIT License

    Copyright (c) 2017 Jacopo De Luca

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
*/

package sprouts.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.LinkedList;
import java.util.List;

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
