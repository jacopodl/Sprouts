package sprouts;

import org.junit.Test;
import sprouts.exceptions.SproutsAccessViolation;
import sprouts.exceptions.SproutsAssignationError;
import sprouts.exceptions.SproutsInvalidClassType;
import sprouts.settings.SproutsSettings;
import sprouts.test_classes.*;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class SproutsTest {
    @Test(expected = SproutsInvalidClassType.class)
    public void instantiateInterfaceError() {
        Sprouts sprouts = new Sprouts();
        sprouts.getInstance(SensorsBox.class);
    }

    @Test(expected = SproutsInvalidClassType.class)
    public void instantiateAbstractClassError() {
        Sprouts sprouts = new Sprouts();
        sprouts.getInstance(AbstractBox.class);
    }

    @Test(expected = SproutsAccessViolation.class)
    public void getInstanceByProviderWithoutPrivateMethodPermission() {
        Sprouts sprouts = new Sprouts(new SproutsSettings() {
            @Override
            public void configure() {
                bind(SensorsBox.class).to(EnvironmentBox.class);
            }
        });
        sprouts.getInstance(SensorsBox.class);
    }

    @Test
    public void getInstanceByProviderWithPrivateMethodPermission() {
        Sprouts sprouts = new Sprouts(new SproutsSettings() {
            @Override
            public void configure() {
                bind(SensorsBox.class).to(EnvironmentBox.class);
                allowPrivateMethod = true;
            }
        });
        sprouts.getInstance(SensorsBox.class);
    }

    @Test(expected = SproutsAssignationError.class)
    public void getInstanceByWrongBind() {
        Sprouts sprouts = new Sprouts(new SproutsSettings() {
            @Override
            public void configure() {
                bind(SensorsBox.class).to(HumiditySensor.class);
            }
        });
        sprouts.getInstance(SensorsBox.class);
    }

    @Test
    public void checkUniversalInstance() {
        TemperatureSensor tSens;
        TempSensorTester tSensTester;

        Sprouts sprouts = new Sprouts(new SproutsSettings() {
            @Override
            public void configure() {
                bind(SensorsBox.class).to(EnvironmentBox.class);
                allowPrivateMethod = true;
                allowPrivateField = true;
            }
        });
        tSens = ((EnvironmentBox) sprouts.getInstance(SensorsBox.class)).getTempSensor();
        tSensTester = (TempSensorTester) sprouts.getInstance(TempSensorTester.class);
        assertSame(tSens, tSensTester.getSensor());
    }

    @Test
    public void checkNewInstance() {
        TemperatureSensor tSens;
        TempSensorTester tSensTester;

        Sprouts sprouts = new Sprouts(new SproutsSettings() {
            @Override
            public void configure() {
                bind(SensorsBox.class).to(EnvironmentBox.class);
                allowPrivateMethod = true;
                allowPrivateField = true;
            }
        });
        tSens = ((EnvironmentBox) sprouts.getInstance(SensorsBox.class)).getTempSensor();
        tSensTester = (TempSensorTester) sprouts.getInstance(TempSensorTester.class);
        assertNotSame(tSens, tSensTester.getNewTSensor());
    }
}