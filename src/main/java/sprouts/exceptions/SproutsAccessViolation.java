package sprouts.exceptions;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SproutsAccessViolation extends RuntimeException {
    public SproutsAccessViolation(Class clazz, AccessibleObject aobj) {
        super(new BuildInfo(clazz, aobj).getErrorMsg());
    }

    private static class BuildInfo {
        private Class clazz;
        private AccessibleObject aobj;

        private BuildInfo(Class clazz, AccessibleObject aobj) {
            this.clazz = clazz;
            this.aobj = aobj;
        }

        private String getErrorMsg() {
            String type;

            if (aobj instanceof Field)
                type = String.format("field(%s)", ((Field) aobj).getName());
            else if (aobj instanceof Constructor)
                type = String.format("constructor(%s)", ((Constructor) aobj).getName());
            else
                type = String.format("method(%s)", ((Method) aobj).getName());

            return "Unable to inject dependency into " + type + " in class " + this.clazz.getCanonicalName();
        }
    }
}
