package Services

import Models.{Product, Products, Purchase, Purchases}
import Database.DatabaseConnection
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

object RecommendationService {
  private val purchases = TableQuery[Purchases]
  private val products  = TableQuery[Products]
  private val db = DatabaseConnection.db

  def getRecommendations(userId: Int)(implicit ec: ExecutionContext): Future[Seq[Product]] = {
    val userPurchases = purchases
      .filter(_.userId === userId)
      .groupBy(_.productId)
      .map { case (productId, group) =>
        (productId, group.map(_.quantity).sum.getOrElse(0))
      }
      .filter(_._2 >= 2)
      .sortBy(_._2.desc)

    val query = for {
      (productId, totalQuantity) <- userPurchases
      product <- products if product.id === productId
    } yield (product, totalQuantity)

    db.run(query.result).map { results =>
      results.map(_._1)
    }
  }
}
