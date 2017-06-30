package sprouts.test_classes;

public class TemperatureSensor implements Sensor<Float> {
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
    public Float readData() {
        return null;
    }
}
