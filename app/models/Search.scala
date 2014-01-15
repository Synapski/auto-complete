package models

object Search {

  private val wordFile = scala.io.Source.fromFile("conf/resources/basic.txt")
  val wordSet = wordFile.mkString.split(", ").toSet
  wordFile.close()

  val wordMap: Map[String, Set[String]] = (1 to 5).foldLeft(Map.empty[String, Set[String]]) { (a,i) =>
    a ++ wordSet.groupBy(_.take(i))
  }

  def recommendation(sentence: String, max: Int): Set[String] = {

    println(sentence)
    val doc = globals.proc.annotate(sentence)
    for (sentence <- doc.sentences) {
      sentence.lemmas.foreach(lemmas => println("Lemmas: " + lemmas.mkString(" ")))
      sentence.tags.foreach(tags => println("POS tags: " + tags.mkString(" ")))
    }

    if (sentence.endsWith(" ")) {
      Set.empty[String]
    } else {
      wordMap.get(sentence.split(" ").last)
        .getOrElse(Set.empty[String])
        .take(max)
    }

  }

}
