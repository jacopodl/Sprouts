package sprouts.test_classes;

public class HumiditySensor implements Sensor<Integer> {

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void disable() {

    }

    @Override
    public void enable() {

    }

    @Override
    public Integer readData() {
        return null;
    }
}
