package starter.controllers

import com.greencatsoft.angularjs.core.{Log, Scope}
import com.greencatsoft.angularjs.extensions.material.{BottomSheet, BottomSheetOptions, Sidenav}
import com.greencatsoft.angularjs.{Angular, Controller, inject}
import org.scalajs.dom
import org.scalajs.dom.document
import starter.services.{Avatar, AvatarService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.UndefOr
import scala.scalajs.js.annotation.{JSExport, JSExportAll}

/**
 * Main Controller for the Angular Material Starter App
 *
 * https://github.com/angular/material-start/blob/master/app/src/avatars/avatarService.js
 */
object AvatarAppController extends Controller {

  override val name = "AppCtrl"

  override type ScopeType = AvatarAppScope

  @inject
  var sidenav: Sidenav = _

  @inject
  var bottomSheet: BottomSheet = _

  @inject
  var log: Log = _

  override def initialize(scope: ScopeType) {
    scope.selected = null
    scope.avatars = js.Array()

    loadAvatars()
  }

  /**
   * Load all available avatars
   */
  def loadAvatars() {
    AvatarService.loadAll.onSuccess {
      case avatars =>
        scope.avatars = avatars.toJSArray
        scope.selected = avatars(0)
    }
  }

  /**
   * Hide or Show the sideNav area
   * @param menuId
   */
  @JSExport
  def toggleSidenav(menuId: String): Unit = sidenav(menuId).toggle()

  /**
   * Select the current avatars
   * @param avatarId
   */
  def selectAvatar(avatarId: Int) {
    scope.selected = scope.avatars(avatarId)
    toggleSidenav("left")
  }

  /**
   * Select the current avatars
   * @param avatar
   */
  @JSExport
  def selectAvatar(avatar: Avatar) {
    scope.selected = avatar
    toggleSidenav("left")
  }

  /**
   * Show the bottom sheet
   * @param event
   */
  @JSExport
  def showActions(event: dom.MouseEvent) {
    val options = new js.Object().asInstanceOf[BottomSheetOptions]
    options.parent = Angular.element(document.getElementById("content"))
    options.template =
      //language=HTML
      """
        |<md-bottom-sheet class="md-list md-has-header">
        |  <md-subheader>Avatar Actions</md-subheader>
        |  <md-list>
        |    <md-item ng-repeat="item in vm.items">
        |      <md-button ng-click="vm.performAction(item)">{{item.name}}</md-button>
        |    </md-item>
        |  </md-list>
        |</md-bottom-sheet>
      """.stripMargin
    options.bindToController = true
    options.controllerAs = "vm"

    class AvatarSheetController(bottomSheet: BottomSheet) {

      @JSExport
      val items: js.Array[Item] = Array(
        Item("Share", "share"),
        Item("Copy", "copy"),
        Item("Impersonate", "impersonate"),
        Item("Singalong", "singalong")
      ).toJSArray

      @JSExport
      def performAction(action: Item): Unit = bottomSheet.hide(action)
    }

    @JSExportAll
    case class Item(name: String, icon: String)

    val avatarSheetController: js.Function1[BottomSheet, AvatarSheetController] =
      (bottomSheet: BottomSheet) => new AvatarSheetController(bottomSheet)

    options.controller = Array[Any]("$mdBottomSheet", avatarSheetController).toJSArray
    options.targetEvent = event

    val f: Future[Item] = bottomSheet.show(options)
    f.onSuccess {
      case clickedItem: Item => log.debug(s"${clickedItem.name} clicked!")
    }
  }

  trait AvatarAppScope extends Scope {

    var selected: UndefOr[Avatar] = js.native

    var avatars: js.Array[Avatar] = js.native
  }

}
