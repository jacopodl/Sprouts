package sprouts.exceptions;

public class SproutsAssignationError extends RuntimeException {
    public SproutsAssignationError(Class base, Class clazz) {
        super(String.format("Unable to assign object! Object %s not extends object %s", clazz.getCanonicalName(), base.getCanonicalName()));
    }
}
