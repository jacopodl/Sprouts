package sprouts.exceptions;

import java.lang.reflect.*;

public class SproutsAccessViolation extends RuntimeException {
    public SproutsAccessViolation(Class clazz, AccessibleObject aobj) {
        super(new BuildInfo().getErrorMsg(clazz, aobj));
    }

    private static class BuildInfo {
        private String getErrorMsg(Class clazz, AccessibleObject aobj) {
            String type;

            if (aobj instanceof Field)
                type = String.format("field(%s)", ((Field) aobj).getName());
            else if (aobj instanceof Constructor)
                type = String.format("constructor(%s)", ((Constructor) aobj).getName());
            else
                type = String.format("method(%s)", ((Method) aobj).getName());

            return String.format("Unable to inject dependency into %s %s in class %s", getModifier(aobj), type, clazz.getCanonicalName());
        }

        private String getModifier(AccessibleObject aobj) {
            Member member = (Member) aobj;

            if (Modifier.isPrivate(member.getModifiers()))
                return "private";
            else if (Modifier.isProtected(member.getModifiers()))
                return "protected";
            return "";
        }
    }
}
