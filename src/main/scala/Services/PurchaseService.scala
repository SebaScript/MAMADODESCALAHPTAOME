package Services

import Models.{Purchase, Purchases}
import Database.DatabaseConnection
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

object PurchaseService {
  private val purchases = TableQuery[Purchases]
  private val db = DatabaseConnection.db

  def recordPurchase(purchase: Purchase)(implicit ec: ExecutionContext): Future[Int] = {
    db.run(purchases returning purchases.map(_.id) += purchase)
  }

  def getPurchaseHistory(userId: Int)(implicit ec: ExecutionContext): Future[Seq[Purchase]] = {
    db.run(purchases.filter(_.userId === userId).result)
  }
}
