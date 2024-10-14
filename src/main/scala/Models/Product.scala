package Models

import slick.jdbc.PostgresProfile.api._

case class Product(id: Option[Int], name: String, description: String, price: Double)

class Products(tag: Tag) extends Table[Product](tag, "products") {
  def id          = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name        = column[String]("name")
  def description = column[String]("description")
  def price       = column[Double]("price")

  def * = (id.?, name, description, price) <> (Product.tupled, Product.unapply)
}

