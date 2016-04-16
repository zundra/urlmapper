// Comment to get more information during initialization
//logLevel := Level.Warn


resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.0")

addSbtPlugin("com.jamesward" %% "play-auto-refresh" % "0.0.14")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.13.0")
