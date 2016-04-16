name := """tdfw"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

routesGenerator := InjectedRoutesGenerator

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-slick" % "2.0.0",
    "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
    "org.reactivemongo" %% "play2-reactivemongo" % "0.11.11",
    "commons-validator" % "commons-validator" % "1.5.0"
)

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

fork in run := true

assemblyMergeStrategy in assembly := {
    case x if x.endsWith("overview.html") => MergeStrategy.first
    case x if x.endsWith("spring.tooling") => MergeStrategy.first
    case x if x.endsWith("overview.html") => MergeStrategy.first
    case x if x.endsWith("spring.tooling") => MergeStrategy.first
    case x if x.contains("com/google/common") => MergeStrategy.first
    case x if x.contains("org/slf4j/impl") => MergeStrategy.first
    case x if x.contains("io.netty.versions.properties") => MergeStrategy.first
    case x if x.contains("META-INF/log4j-provider.properties") => MergeStrategy.first
    case x if x.contains("/commons/logging") => MergeStrategy.first
    case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
}