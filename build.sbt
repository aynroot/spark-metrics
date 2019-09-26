import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.auth.{EnvironmentVariableCredentialsProvider, InstanceProfileCredentialsProvider}
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.model.CannedAccessControlList
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

organization := "io.relayr"

name := "analytics-spark-metrics"

scalaVersion := "2.11.12"

crossScalaVersions := Seq("2.11.12")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:existentials", "-Ywarn-unused-import")

val sparkVersion = "2.4.0"
val jettyVersion = "9.3.24.v20180605"

libraryDependencies ++= Seq(
  "io.dropwizard.metrics" % "metrics-core" % "3.1.2",
  "org.apache.spark" %% "spark-core" % sparkVersion % Provided,
  "org.apache.spark" %% "spark-streaming" % sparkVersion % Provided,
  "org.eclipse.jetty" % "jetty-servlet" % jettyVersion % Provided,
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)

publishMavenStyle := false

parallelExecution in Test := false

s3region := Regions.EU_WEST_1
s3acl := CannedAccessControlList.Private
s3credentials := new InstanceProfileCredentialsProvider(false) | new ProfileCredentialsProvider() | new EnvironmentVariableCredentialsProvider()

publishTo := Some(s3resolver.value("relayr-maven", s3("relayr-maven")))
publishArtifact in (Compile, packageDoc) := false

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  setNextVersion,
  commitNextVersion,
  pushChanges
)
