# Expression Calculator
A simple integer expression processor, using ParsecJ (a monadic parser combinator; a Java port of Haskell's Parsec library). The application was written by [Rodrigo Lopez-Guzman](https://linkedin.com/in/rlogman).


## How to Build
This application is a Maven project and uses Spring boot as a quick starting point. To build it, use:
```bash
mvn install
```
This generates a .jar containing all the classes and configuration files required to use the application.

## How to Distribute
After building, go to the target directory and grab `calculator-0.0.1-SNAPSHOT.jar`. This is a standard JAR file containing all necessary dependencies.

## How to Run
Once you have the application built, you can running this way:
```bash
java -jar target/calculator-0.0.1-SNAPSHOT.jar "sum(5, 7)"
```
Of course, you should change the expression to whatever you want to evaluate. And the .jar file could be located anywhere in your file system.

## Configuring Verbosity though Log Levels
The application has a very flexible mechanism to configure which logs we want to see. The logging functionality is totally pluggable/unpluggable and could be configured even to generate the output to other destinations instead of the standard output, as it is set up by default. The application inherits all this flexibility from Spring and you can refer to Spring documentation for further customization.

One way we could use to configure a granular output of the application's own classes could be:
```bash
java -jar target/calculator-0.0.1-SNAPSHOT.jar  --logging.level.calculator=TRACE "sum(5, 7)"
```
The accepted levels are: `TRACE`, `DEBUG`, `INFO`, `WARN`, `ERROR`, `FATAL` and `OFF`. The global logging is configured by default as `OFF` in the application, but passing `--logging.level.*` options you could ask the application to show logs according to your needs. You can even combine multiple `--logging.level.*` options, to tailor the output to what you want to see or hide.

