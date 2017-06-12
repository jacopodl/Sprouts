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

package sprouts;

import sprouts.annotation.BindWith;
import sprouts.annotation.GetInstance;
import sprouts.annotation.New;
import sprouts.exceptions.SproutsMultipleConstructors;
import sprouts.settings.Settings;
import sprouts.settings.ProviderInfo;
import sprouts.support.FakeAnnotate;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class Sprouts {
    private Settings settings;
    private Map<String, Object> savedInstance;

    public Sprouts() {
        this(null);
    }

    public Sprouts(Settings settings) {
        this.savedInstance = new HashMap<>();
        this.settings = settings;
        if (settings != null)
            settings.configure();
    }


    private Object objectBuilder(Class clazz) throws Exception {
        Object obj;

        // STEP 1
        obj = constructorInjector(clazz);
        // STEP 2
        fieldInjector(obj, clazz);
        // STEP 3
        // METHOD

        return obj;
    }

    private Object objectBuilder(Class clazz, AnnotatedElement ae) throws Exception {
        Class iClass;
        Object obj;
        boolean newInstance = ae.isAnnotationPresent(New.class);

        iClass = ClassLoader.getSystemClassLoader().loadClass(getQualifier(clazz, ae.getAnnotation(BindWith.class)));

        if (!newInstance && this.savedInstance.containsKey(iClass.getCanonicalName()))
            return this.savedInstance.get(iClass.getCanonicalName());

        if (newInstance)
            return objectBuilder(iClass);

        obj = objectBuilder(iClass);
        this.savedInstance.put(iClass.getCanonicalName(), obj);
        return obj;
    }

    @SuppressWarnings(value = "unchecked")
    private Object constructorInjector(Class clazz) throws Exception {
        Object[] instancedParam;
        Object newInstance = null;
        boolean isPrivate;
        int idx = 0;

        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (newInstance != null)
                throw new SproutsMultipleConstructors(clazz);

            if (constructor.isAnnotationPresent(GetInstance.class)) {
                instancedParam = new Object[constructor.getParameterCount()];
                isPrivate = constructor.isAccessible();
                for (Parameter param : constructor.getParameters())
                    instancedParam[idx++] = objectBuilder(param.getType(), param);
                newInstance = constructor.newInstance(instancedParam);
                constructor.setAccessible(isPrivate);
            }
        }

        if (newInstance != null)
            return newInstance;

        Constructor<?> stdConstructor = clazz.getDeclaredConstructor();
        isPrivate = stdConstructor.isAccessible();
        stdConstructor.setAccessible(true);
        newInstance = stdConstructor.newInstance();
        stdConstructor.setAccessible(isPrivate);

        return newInstance;
    }

    private String getQualifier(Class clazz, BindWith bw) {
        ProviderInfo info = null;

        if (this.settings != null)
            info = this.settings.getMapping(clazz);

        if (bw == null) {
            if (info != null)
                return info.getQualifier().getCanonicalName();
            else
                return clazz.getCanonicalName();
        }

        if (info != null && info.getOverride())
            return info.getQualifier().getCanonicalName();
        return bw.className();
    }

    private void fieldInjector(Object obj, Class clazz) throws Exception {
        Object toInject = obj;
        boolean isPrivate;

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(GetInstance.class)) {
                isPrivate = field.isAccessible();
                field.setAccessible(true);
                if (!field.getType().equals(clazz))
                    toInject = objectBuilder(field.getType(), field);
                field.set(obj, toInject);
                field.setAccessible(isPrivate);
            }
        }
    }

    private Object getInstance(Class clazz, boolean getNew) {
        Object obj = null;
        FakeAnnotate annotate;

        try {
            annotate = new FakeAnnotate();
            if (getNew) {
                annotate.addAnnotation(new New() {

                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return New.class;
                    }
                });
            }
            obj = clazz.cast(objectBuilder(clazz, annotate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public Object getInstance(Class clazz) {
        return getInstance(clazz, false);
    }

    public Object getNewInstance(Class clazz) {
        return getInstance(clazz, true);
    }
}
