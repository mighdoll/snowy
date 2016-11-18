# Snowy

An experimental WebSocket game using [Scala](http://scala-lang.org) and [Scala.js](http://scala-js.org)

1. [Cloning / Downloading](#cloning--downloading)
2. [Running](#running)
3. [Customization](#customization)

### Cloning / Downloading

1. Download Java: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
2. Download SBT: http://www.scala-sbt.org/
3. Clone the git repository: `git clone https://github.com/mighdoll/snowy.git`

### Running

1. Cd into the project: `cd snowy`
2. Run snowy: `sbt run` or `sbt reStart shell`

### Customization

* To spawn test users run `sbt it:run`
* Change port inside this file: `server/src/main/resources/application.conf`
* Change game features in this file: `shared/src/main/scala/snowy/GameConstants.scala`

###Credits

#####This project was created by:

* [Mighdoll](https://github.com/mighdoll)
*  [ModderMe123](https://github.com/modderme123)

#####With massive help from:

* [Coler706](https://github.com/coler706)
* [{Name hidden}]()

 
##### [![YourKit Logo](https://www.yourkit.com/images/yklogo.png)](https://yourkit.com)
[YourKit](https://www.yourkit.com/) kindly supports open source projects with its full-featured [Java Profiler](https://www.yourkit.com/java/profiler/index.jsp) and [.NET Profiler](https://www.yourkit.com/.net/profiler/index.jsp). We turn to YourKit's [Java Profiler](https://www.yourkit.com/java/profiler/index.jsp) as the standard reference for code performance, quickly monitoring garbage collection, leak analysis, and more. 
