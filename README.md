# Project OOP - Vladislav Tiftilov

## Implementation

* Dependency Injection - used to instantiate beans. Beans differ by two parameters, lazy and prototype.
  Lazy means that bean won't be instantiated when Container.initContainer() is called. Prototype means that bean won't
  be stored in container and created each time you request a bean.
* Adapter - to implement services and repositories (to expose an easier interface firstly to work with files and after
  that with repos)
* Proxy - to log information about BattlePokemon (wasAttacked, won, lost)
* Producer/Consumer - multithreaded battle (in ApplicationRunner is being started a thread "Arena" that starts 2 thread
  for each Pokemon. Each thread generates events and reads them from the enemy thread).

Optional:

* DI - using spring

## Remarks

* All paths to input files are hardcoded, therefore for the tests to pass please make sure that no input file is missing
  and weren't modified.
* For unit testing is used `junit`.
* To see all logged information in `src/main/resources/log4j2.xml` replace `DEBUG` level to `TRACE`. Logs could be found
  in root directory.
* Project is compiled with `maven`.
* Requires java `1.8`.

## Compile project

In root directory run:

```shell
mvn clean install
```

## Run tests

In root directory run:

```shell
mvn clean test
```
