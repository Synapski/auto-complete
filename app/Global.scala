import play.api._
import edu.arizona.sista.processor.Processor
import edu.arizona.sista.processor.corenlp.CoreNLPProcessor

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")
    globals.proc = new CoreNLPProcessor()
    globals.proc.annotate("Test")
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

}

package object globals {
  var proc: Processor = null
}