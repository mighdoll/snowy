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


#####This project is using YourKit: 

YourKit supports open source projects with its full-featured Java Profiler.  
YourKit, LLC is the creator of YourKit Java Profiler and YourKit .NET Profiler,  
innovative and intelligent tools for profiling Java and .NET applications.  
