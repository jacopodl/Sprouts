package sprouts.exceptions;

public class SproutsMultipleConstructors extends RuntimeException {
    public SproutsMultipleConstructors(Class clazz) {
        super("Multiple constructors annotated with @GetInstance in class: " + clazz.getCanonicalName());
    }
}
