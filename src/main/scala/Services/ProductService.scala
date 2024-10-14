package Services

import Models.{Product, Products}
import Database.DatabaseConnection
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

object ProductService {
  private val products = TableQuery[Products]
  private val db = DatabaseConnection.db

  def createProduct(product: Product)(implicit ec: ExecutionContext): Future[Int] = {
    db.run(products returning products.map(_.id) += product)
  }

  def updateProduct(product: Product)(implicit ec: ExecutionContext): Future[Int] = {
    db.run(products.filter(_.id === product.id).update(product))
  }

  def deleteProduct(productId: Int)(implicit ec: ExecutionContext): Future[Int] = {
    db.run(products.filter(_.id === productId).delete)
  }
}
