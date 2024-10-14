package Models

import slick.jdbc.PostgresProfile.api._

case class Purchase(id: Option[Int], userId: Int, productId: Int, quantity: Int)

class Purchases(tag: Tag) extends Table[Purchase](tag, "purchases") {
  def id        = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def userId    = column[Int]("user_id")
  def productId = column[Int]("product_id")
  def quantity  = column[Int]("quantity")

  def * = (id.?, userId, productId, quantity) <> (Purchase.tupled, Purchase.unapply)

  def userFK    = foreignKey("user_fk", userId, TableQuery[Users])(_.id)
  def productFK = foreignKey("product_fk", productId, TableQuery[Products])(_.id)
}
