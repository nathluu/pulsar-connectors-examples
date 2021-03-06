resolvers ++= Seq (
  Resolver.mavenLocal,
  DefaultMavenRepository,
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Stream native Bintray" at "https://dl.bintray.com/streamnative/maven"
)

lazy val root = (project in file(".")).
  settings(
    name := "pulsarspark",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.11.12",
//    assembly / test := {},
    mainClass in Compile := Some("com.example.spark.jobs.WordCount")
//    mainClass in assembly := Some("com.example.spark.jobs.WordCount")
  )

val sparkVersion = "2.4.5"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "io.streamnative.connectors" %% "pulsar-spark-connector" % "2.4.6-SNAPSHOT" % "provided"
//  "io.streamnative.connectors" %% "pulsar-spark-connector" % "2.4.5"
)

//assemblyMergeStrategy in assembly := {
//  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
//  case x => MergeStrategy.first
//}

//assembly / assemblyMergeStrategy := (x => MergeStrategy.first)
//
//assemblyShadeRules in assembly := Seq(
//  ShadeRule.rename("io.netty.**" -> "shadeio.@1").inAll,
//  ShadeRule.rename("com.fasterxml.jackson.**" -> "shadeio.@1").inAll
//)
