package starter

import com.greencatsoft.angularjs.extensions.material.ThemingProvider
import com.greencatsoft.angularjs.{Angular, Config, inject}
import starter.controllers.{AvatarAppController, AvatarSheetController}

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

/** @see https://github.com/angular/material-start/blob/master/app/app.js */
@JSExport
object StarterApp extends JSApp with Config {

  @inject
  var themingProvider: ThemingProvider = _

  override def main() {
    val module = Angular.module("starterApp", Seq("ngMaterial"))
    module.controller(AvatarSheetController)
    module.controller(AvatarAppController)
    module.config(this)
  }

  override def initialize(): Unit = {
    themingProvider.theme("default")
      .primaryPalette("brown")
      .accentPalette("brown")
  }
}
