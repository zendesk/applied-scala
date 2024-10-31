name := "applied-scala"

version := "1.0"

scalaVersion := "3.3.3"

Compile / run / mainClass := Some("com.reagroup.appliedscala.Main")

val catsVersion = "2.12.0"
val circeVersion = "0.14.10"
val http4sVersion = "0.23.29"
val logbackVersion = "1.5.12"
val postgresqlVersion = "42.7.4"
val doobieVersion = "1.0.0-RC6"
val specs2Version = "4.20.9"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  // for auto-derivation of JSON codecs
  "io.circe" %% "circe-generic",
  // for string interpolation to JSON model
  "io.circe" %% "circe-literal"
).map(_ % circeVersion)

libraryDependencies ++= Seq(
  "org.typelevel"           %% "cats-core"              % catsVersion,
  "org.http4s"              %% "http4s-ember-server"    % http4sVersion,
  "org.http4s"              %% "http4s-ember-client"    % http4sVersion,
  "org.http4s"              %% "http4s-circe"           % http4sVersion,
  "org.http4s"              %% "http4s-dsl"             % http4sVersion,
  "ch.qos.logback"           % "logback-classic"        % logbackVersion,
  "org.postgresql"           % "postgresql"             % postgresqlVersion,
  "org.tpolecat"            %% "doobie-core"            % doobieVersion,
  "org.tpolecat"            %% "doobie-postgres"        % doobieVersion,
  "org.specs2"              %% "specs2-core"            % specs2Version % Test,
  "org.specs2"              %% "specs2-matcher-extra"   % specs2Version % Test,
  "org.specs2"              %% "specs2-scalacheck"      % specs2Version % Test
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-language:higherKinds"
)

testFrameworks := Seq(TestFrameworks.Specs2)
