![](https://travis-ci.org/jacopodl/Sprouts.svg?branch=master)
# Sprouts #

Sprouts is designed for projects that don't need a extremely complex DI.

Actually Sprouts implements this features:

* Field injection
* Constructor injection
* Method injection

# How to use #

## Add library to your project ##
1. Download Sprouts from this repository and compile your own .jar library.
1. Download compiled .jar file from the [release](https://github.com/jacopodl/Sprouts/releases/) page.

## Quick tour ##

### Write your classes ###

When your class needs an instance of another class, simply annotate with `@GetInstance` annotation the class field.
```java

public class TemperatureSensor
{
	private boolean status;

	public TemperatureSensor() {
		this.status = true;
	}

	public boolean isEnabled() {
		return this.status;
	}

	public float readTemp() {
		return ...;
	}

	public void disable() {
		this.status = false;
	}

	public void enable() {
		this.status = true;
	}
}

public class SensorsBox
{
	@GetInstance
	@Exposed
	private TemperatureSensor tSens;

	public void publishData() {
		publish(this.tSens.readTemp());
	}
}

```
### Main app ###
```java

public class MagicSensorApp
{
	public static void main(String[] args)
	{
		Sprouts srpouts = new Sprouts();
		SensorsBox sensBox = (SensorsBox) sprouts.getInstance();
		...
		sensBox.publishData();
		...
	}
}
```

# Circular Dependencies #

Creating circular dependencies is generally a bad idea and Sprouts doesn't support these operation.

If you try to create the following sitution:

```java

public class Alpha {
	@GetInstance
	Beta beta;
}

public class Beta {
	@GetInstance
	Gamma gamma;
}

public class Gamma {
	@GetInstance
	Alpha alpha;
}
```
your program going to crash with `java.lang.StackOverflowError` exception, in this case please fix your dependencies graph to avoid this!
