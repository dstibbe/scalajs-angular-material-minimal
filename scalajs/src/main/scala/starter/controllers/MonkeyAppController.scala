package starter.controllers

import starter.model.Monkey
import starter.scopes.MonkeyScope
import com.greencatsoft.angularjs.core.{Log, Scope}
import com.greencatsoft.angularjs.{Angular, Controller, inject, injectable}
import org.scalajs.dom
import org.scalajs.dom.document

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.UndefOr
import scala.scalajs.js.annotation.{JSExport, JSExportAll}

@injectable("AppCtrl")
object MonkeyAppController extends Controller[MonkeyScope] {

  @inject
  var scope: MonkeyScope = _

  @inject
  var log: Log = _

  override def initialize() {
    super.initialize()

    scope.monkeys = loadMonkeys().toJSArray
  }
	
  @JSExport	
  def showMonkeys() = {
	 scope.monkeys
  }
  
  
  /**
   * Load all monkeys
   */
  def loadMonkeys() = {
     Array(
		Monkey("George", "silent", "red"),
		Monkey("Clooney", "talkative", "green"),
		Monkey("Ray", "blind", "yellow"),
		Monkey("Charles", "deaf", "purple")
	 )
  }
}