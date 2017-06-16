package sprouts.exceptions;

import java.lang.reflect.Modifier;

public class SproutsInvalidClassType extends RuntimeException {
    public SproutsInvalidClassType(Class clazz) {
        super(new BuildInfo(clazz).getErrorMsg());
    }

    private static class BuildInfo {
        private Class clazz;

        private BuildInfo(Class clazz) {
            this.clazz = clazz;
        }

        private String getErrorMsg() {
            String type = "";

            if (Modifier.isAbstract(clazz.getModifiers()))
                type = "abstract";
            else if (Modifier.isInterface(clazz.getModifiers()))
                type = "interface";

            return "Unable to build object from class " + clazz.getCanonicalName() + " because this class is " + type + ". Use @BindWith!";
        }
    }
}
