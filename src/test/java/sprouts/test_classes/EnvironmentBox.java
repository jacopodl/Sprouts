package sprouts.test_classes;


import sprouts.annotation.Exposed;
import sprouts.annotation.GetInstance;

public class EnvironmentBox implements SensorsBox {
    @GetInstance
    @Exposed
    private TemperatureSensor tSens;

    private PressureSensor pSens;

    private HumiditySensor hSens;

    @GetInstance
    public EnvironmentBox(PressureSensor pSens) {
        this.pSens = pSens;
    }

    @GetInstance
    private void setHSens(HumiditySensor hSens) {
        this.hSens = hSens;
    }

    @Override
    public void publishData() {

    }

    @Override
    public void disableAllSensors() {

    }

    @Override
    public void enableAllSensors() {

    }

    public TemperatureSensor getTempSensor() {
        return this.tSens;
    }
}
