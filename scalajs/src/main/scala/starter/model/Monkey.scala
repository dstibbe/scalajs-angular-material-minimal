package starter.model

import com.greencatsoft.angularjs.core.{Log, Scope}
import com.greencatsoft.angularjs.{Angular, Controller, inject, injectable}

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.annotation.{JSExport, JSExportAll}

@JSExportAll
case class Monkey(name: String, action: String, color: String)