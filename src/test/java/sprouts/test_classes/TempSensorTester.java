package sprouts.test_classes;

import sprouts.annotation.GetInstance;
import sprouts.annotation.New;

public class TempSensorTester {
    @GetInstance
    private TemperatureSensor tSens;

    @GetInstance
    @New
    private TemperatureSensor newTSens;


    public boolean isWorkCorrectly() {
        return true;
    }

    public TemperatureSensor getSensor() {
        return this.tSens;
    }

    public TemperatureSensor getNewTSensor() {
        return this.newTSens;
    }
}
