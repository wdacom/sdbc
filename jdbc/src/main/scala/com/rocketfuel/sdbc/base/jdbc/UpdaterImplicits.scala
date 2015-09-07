package com.rocketfuel.sdbc.base.jdbc

trait UpdaterImplicits {

  implicit def UpdaterToOptionUpdater[T](implicit updater: Updater[T]): Updater[Option[T]] = {
    new Updater[Option[T]] {
      override def update(row: UpdatableRow, columnIndex: Int, x: Option[T]): Unit = {
        x match {
          case None =>
            row.updateObject(columnIndex, null)
          case Some(value) =>
            updater.update(row, columnIndex, value)
        }
      }
    }
  }

  /**
   * This implicit is used if None is used on the right side of an update.
   *
   * {{{
   *   val row: MutableRow = ???
   *
   *   row("columnName") = None
   * }}}
   */
  implicit val NoneUpdater: Updater[None.type] = new Updater[None.type] {
    override def update(row: UpdatableRow, columnIndex: Int, x: None.type): Unit = {
      row.updateObject(columnIndex, null)
    }
  }

}
