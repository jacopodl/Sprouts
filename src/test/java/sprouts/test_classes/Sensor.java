package sprouts.test_classes;


public interface Sensor<T> {
    boolean isEnabled();

    void disable();

    void enable();

    T readData();
}
