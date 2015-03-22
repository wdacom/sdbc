package com.wda.sdbc.base

trait AbstractDeployable {
  self: Connection with ParameterValue with AbstractQuery with Update =>

  /**
   * Deployable doesn't quite fit in the minimalist design philosophy of
   * this library, but I find it incredibly useful to group queries
   * together in chunks that create and delete things that belong together.
   *
   * For instance, a deployable can create a schema, and another deployable
   * can create a table in that schema. You can put them together in a Seq
   * and then use .create() and .drop() on that Seq.
   */
  trait AbstractDeployable {

    val createStatements: Iterable[Update]
    val dropStatements: Iterable[Update]

    def create()(implicit connection: Connection): Unit = {
      createStatements.foreach(_.execute())
    }

    def drop()(implicit connection: Connection): Unit = {
      dropStatements.foreach(_.execute())
    }

    def ++(that: AbstractDeployable): AbstractDeployable = {
      new AbstractDeployable {

        override def create()(implicit connection: Connection): Unit = {
          super.create()
          that.create()
        }

        override def drop()(implicit connection: Connection): Unit = {
          that.drop()
          super.drop()
        }

        override val createStatements: Iterable[Update] = {
          createStatements ++ that.createStatements
        }
        override val dropStatements: Iterable[Update] = {
          that.dropStatements ++ dropStatements
        }
      }
    }
  }

  case class Deployable(
    createStatements: Iterable[Update],
    dropStatements: Iterable[Update]
  ) extends AbstractDeployable

  implicit class DeployableSeq(deployables: Seq[AbstractDeployable]) {
    def create()(implicit connection: Connection): Unit = {
      deployables.foreach(_.create())
    }

    def drop()(implicit connection: Connection): Unit = {
      deployables.reverse.foreach(_.drop())
    }
  }

}
