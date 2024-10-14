package Services

import Models.{User, Users}
import Database.DatabaseConnection
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

object UserService {
  private val users = TableQuery[Users]
  private val db = DatabaseConnection.db

  def createUser(user: User)(implicit ec: ExecutionContext): Future[Int] = {
    val normalizedUser = normalizeUserData(user)
    db.run(users returning users.map(_.id) += normalizedUser)
  }

  def normalizeUserData(user: User): User = {
    val normalizedEmail = user.email.trim.toLowerCase
    val normalizedName  = user.name.trim
    user.copy(name = normalizedName, email = normalizedEmail)
  }
}
