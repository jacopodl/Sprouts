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

import sprouts.annotation.BindTo;
import sprouts.annotation.Exposed;
import sprouts.annotation.GetInstance;
import sprouts.annotation.New;
import sprouts.exceptions.*;
import sprouts.settings.DefaultSproutsSettings;
import sprouts.settings.ProviderInfo;
import sprouts.settings.SproutsSettings;
import sprouts.support.FakeAnnotate;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class Sprouts {
    private SproutsSettings settings;
    private Map<String, Object> savedInstance;

    public Sprouts() {
        this(new DefaultSproutsSettings());
    }

    public Sprouts(SproutsSettings settings) {
        this.savedInstance = new HashMap<>();
        this.savedInstance.put(this.getClass().getCanonicalName(), this);
        this.settings = settings;
        this.settings.configure();
    }

    private Class loadClass(String className) {
        try {
            return ClassLoader.getSystemClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new SproutsClassNotFound(className);
        }
    }

    @SuppressWarnings(value = "unchecked")
    private Object objectBuilder(Class clazz, AnnotatedElement ae) {
        Class iClass;
        Object obj;
        boolean newInstance = ae.isAnnotationPresent(New.class);

        iClass = loadClass(getQualifier(clazz, ae.getAnnotation(BindTo.class)));

        if (!clazz.isAssignableFrom(iClass))
            throw new SproutsAssignationError(clazz, iClass);

        if (!newInstance) {
            synchronized (this) {
                if (this.savedInstance.containsKey(iClass.getCanonicalName()))
                    return this.savedInstance.get(iClass.getCanonicalName());
            }
        }

        // STEP 1
        obj = constructorInjector(iClass);

        // STEP 2
        fieldInjector(obj, iClass);

        // STEP 3
        methodInjector(obj, iClass);

        if (newInstance)
            return obj;

        synchronized (this) {
            this.savedInstance.put(iClass.getCanonicalName(), obj);
        }
        return obj;
    }

    @SuppressWarnings(value = "unchecked")
    private Object constructorInjector(Class clazz) {
        Object[] instancedParam;
        Object newInstance = null;
        boolean isAccessible;
        int idx = 0;

        if (Modifier.isAbstract(clazz.getModifiers()) || Modifier.isInterface(clazz.getModifiers()))
            throw new SproutsInvalidClassType(clazz);

        try {
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                if (newInstance != null)
                    throw new SproutsMultipleConstructors(clazz);

                if (constructor.isAnnotationPresent(GetInstance.class)) {
                    if (disallowInject(constructor, this.settings.getAllowPrivateConstructor()))
                        throw new SproutsAccessViolation(clazz, constructor);
                    instancedParam = new Object[constructor.getParameterCount()];
                    isAccessible = constructor.isAccessible();
                    for (Parameter param : constructor.getParameters())
                        instancedParam[idx++] = objectBuilder(param.getType(), param);
                    newInstance = constructor.newInstance(instancedParam);
                    constructor.setAccessible(isAccessible);
                }
            }

            if (newInstance != null)
                return newInstance;

            Constructor<?> stdConstructor = clazz.getDeclaredConstructor();
            isAccessible = stdConstructor.isAccessible();
            stdConstructor.setAccessible(true);
            newInstance = stdConstructor.newInstance();
            stdConstructor.setAccessible(isAccessible);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }

        return newInstance;
    }

    private void methodInjector(Object obj, Class clazz) {
        Object[] instancedParam;
        boolean isAccessible;
        int idx = 0;

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(GetInstance.class)) {
                if (disallowInject(method, this.settings.getAllowPrivateMethod()))
                    throw new SproutsAccessViolation(clazz, method);
                instancedParam = new Object[method.getParameterCount()];
                isAccessible = method.isAccessible();
                method.setAccessible(true);
                for (Parameter param : method.getParameters())
                    instancedParam[idx++] = objectBuilder(param.getType(), param);
                try {
                    method.invoke(obj, instancedParam);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                method.setAccessible(isAccessible);
                idx = 0;
            }
        }
    }

    private boolean disallowInject(AccessibleObject aobj, boolean permission) {
        Member member = (Member) aobj;
        return !aobj.isAnnotationPresent(Exposed.class) && ((Modifier.isPrivate(member.getModifiers()) || Modifier.isProtected(member.getModifiers())) && !permission);
    }

    private String getQualifier(Class clazz, BindTo bw) {
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

    private void fieldInjector(Object obj, Class clazz) {
        Object toInject = obj;
        boolean isAccessible;

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(GetInstance.class)) {
                if (disallowInject(field, this.settings.getAllowPrivateField()))
                    throw new SproutsAccessViolation(clazz, field);
                isAccessible = field.isAccessible();
                field.setAccessible(true);
                if (!field.getType().equals(clazz))
                    toInject = objectBuilder(field.getType(), field);
                try {
                    field.set(obj, toInject);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                field.setAccessible(isAccessible);
            }
        }
    }

    private Object getInstance(Class clazz, boolean getNew) {
        Object obj;
        FakeAnnotate annotate;

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

        return obj;
    }

    public Object getInstance(Class clazz) {
        return getInstance(clazz, false);
    }

    public Object getNewInstance(Class clazz) {
        return getInstance(clazz, true);
    }
}
