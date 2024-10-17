package Services

import Models.{Product, Products, Purchase, Purchases}
import Database.DatabaseConnection
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}
import com.typesafe.scalalogging.LazyLogging

object RecommendationService extends LazyLogging {
  private val purchases = TableQuery[Purchases]
  private val products  = TableQuery[Products]
  private val db = DatabaseConnection.db

  // Utility method to time Futures
  def timeFuture[T](blockName: String)(block: => Future[T])(implicit ec: ExecutionContext): Future[T] = {
    val startTime = System.nanoTime()
    val result = block
    result.map { res =>
      val endTime = System.nanoTime()
      val duration = (endTime - startTime) / 1e6
      logger.info(f"$blockName executed in $duration%.2f ms")
      res
    }
  }

  def getRecommendations(userId: Int)(implicit ec: ExecutionContext): Future[Seq[Product]] = {
    timeFuture("getRecommendations") {
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
}
