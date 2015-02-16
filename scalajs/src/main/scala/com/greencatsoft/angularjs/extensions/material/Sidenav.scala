package com.greencatsoft.angularjs.extensions.material

import com.greencatsoft.angularjs.injectable

import scala.scalajs.js

/**
 * $mdSidenav makes it easy to interact with multiple sidenavs in an app.
 *
 * https://material.angularjs.org/#/api/material.components.sidenav/service/$mdSidenav
 */
@injectable("$mdSidenav")
trait Sidenav extends js.Object {

  def apply(componentId: String): this.type = js.native

  /**
   * Toggle the given sidenav
   */
  def toggle() = js.native

  /**
   * Open the given sidenav
   */
  def open() = js.native

  /**
   * Close the given sidenav
   */
  def close() = js.native

  /**
   * Exposes whether given sidenav is set to be open
   */
  def isOpen = js.native

  /**
   * Exposes whether given sidenav is locked open
   * If this is true, the sidenav will be open regardless of isOpen()
   */
  def isLockedOpen() = js.native
}