name:="FinancialApp"
version:="0.1"
scalaVersion:="2.11.7"

libraryDependencies ++= {

  val akkaVersion       = "2.3.15"
  val ScalaLoggingVersion = "3.7.1"

  Seq(
    "com.typesafe.akka"               %"akka-actor_2.11"            %"2.4.4",
    "com.typesafe.akka"               %% "akka-slf4j"               % akkaVersion,
    "org.slf4j"                       % "slf4j-api"                 % "1.7.5",
    "ch.qos.logback"                  % "logback-classic"           % "1.0.9",
    "com.typesafe.akka"               %% "akka-stream-kafka"        % "0.21.1",
    "org.scalatest"                   % "scalatest_2.11"            % "2.2.1",
    "com.typesafe.scala-logging"      %% "scala-logging"            % ScalaLoggingVersion,
    "org.scalaj"                      %% "scalaj-http"              % "2.4.2",
    "com.typesafe.play"               %% "play-json"                % "2.4.8",
    "com.typesafe.akka"               %% "akka-http-spray-json"    % "10.1.11",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.5.3",
    "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.8.8"



  )
}