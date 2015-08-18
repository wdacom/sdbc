package com.wda.sdbc.base.jdbc

trait UpdaterImplicits {

  implicit def UpdaterToOptionUpdater[T](implicit updater: Updater[T]) = {
    new Updater[Option[T]] {
      override def update(row: MutableRow, columnIndex: Int, x: Option[T]): Unit = {
        x match {
          case None =>
            row.updateObject(columnIndex, null)
          case Some(value) =>
            updater.update(row, columnIndex, value)
        }
      }
    }
  }

}
