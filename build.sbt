name := "auto-complete"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "edu.stanford.nlp" % "stanford-corenlp" % "3.3.0" artifacts (Artifact("stanford-corenlp", "models"), Artifact("stanford-corenlp")),
  "edu.arizona.sista" % "processors" % "1.4"
)

play.Project.playScalaSettings
