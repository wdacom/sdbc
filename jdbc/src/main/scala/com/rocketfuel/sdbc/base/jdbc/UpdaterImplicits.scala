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

  implicit val NoneUpdater: Updater[None.type] = new Updater[None.type] {
    override def update(row: UpdatableRow, columnIndex: Int, x: None.type): Unit = {
      row.updateObject(columnIndex, null)
    }
  }

}
