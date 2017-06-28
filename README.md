# Sprouts #
Fast and lightway dependency injector (DI) for Java projects.
______________________________________________________________

Sprouts is designed for projects thaht don't need a extremely powerfull DI.

Actually Sprouts implements this features:

* Field injection
* Constructor injection
* Method injection

# How to use #

## Add library to your project ##

1. If you use a build-system like Gradle, Sprouts is available in the Maven Central Repository.
2. Download Sprouts from this repository and compile your own .jar library.
3. Download compiled .jar file from the release page.

## Simple example ##


```
#!java

public class MainApp
{
	public static void main(String[] args)
	{
		Sprouts srpouts = new Sprouts(new SproutsSettings()
		{
			public void configure(){
				bind(...).to(...);
			}
		})
	}
}
```