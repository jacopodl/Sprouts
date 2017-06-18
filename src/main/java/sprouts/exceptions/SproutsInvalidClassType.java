package sprouts.exceptions;

import java.lang.reflect.Modifier;

public class SproutsInvalidClassType extends RuntimeException {
    public SproutsInvalidClassType(Class clazz) {
        super(new BuildInfo().getErrorMsg(clazz));
    }

    private static class BuildInfo {
        private String getErrorMsg(Class clazz) {
            String type = "";

            if (Modifier.isAbstract(clazz.getModifiers()))
                type = "abstract";
            else if (Modifier.isInterface(clazz.getModifiers()))
                type = "interface";

            return String.format("Unable to build object from class %s because this class is %s.", clazz.getCanonicalName(), type);
        }
    }
}
