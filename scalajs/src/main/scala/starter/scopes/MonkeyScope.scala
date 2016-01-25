package starter.scopes

import starter.model.Monkey
import com.greencatsoft.angularjs.core.{Log, Scope}
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.UndefOr
import scala.scalajs.js.annotation.{JSExport, JSExportAll}


trait MonkeyScope extends Scope {

  var monkeys: js.Array[Monkey] = js.native
}