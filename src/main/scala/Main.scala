package main

import Services._
import Models._
import scala.io.StdIn
import Database.DatabaseConnection
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {

  val db = DatabaseConnection.db

  val schemas = TableQuery[Users].schema ++
    TableQuery[Products].schema ++
    TableQuery[Purchases].schema

  val setup = DBIO.seq(schemas.createIfNotExists)

  Await.result(db.run(setup), Duration.Inf)

  def menu(): Unit = {
    println("Welcome to the Recommendation System")
    println("1. Create User")
    println("2. Create Product")
    println("3. Update Product")
    println("4. Delete Product")
    println("5. Record Purchase")
    println("6. View Purchase History")
    println("7. Get Recommendations")
    println("0. Exit")
    print("Select an option: ")

    StdIn.readLine() match {
      case "1" => createUser()
      case "2" => createProduct()
      case "3" => updateProduct()
      case "4" => deleteProduct()
      case "5" => recordPurchase()
      case "6" => viewPurchaseHistory()
      case "7" => getRecommendations()
      case "0" => sys.exit()
      case _   => println("Invalid option"); menu()
    }
  }

  def createUser(): Unit = {
    print("Enter name: ")
    val name = StdIn.readLine()
    print("Enter email: ")
    val email = StdIn.readLine()

    val user = User(None, name, email)
    val result = UserService.createUser(user)
    Await.result(result, 5.seconds)
    println("User created successfully.")
    menu()
  }

  def createProduct(): Unit = {
    print("Enter product name: ")
    val name = StdIn.readLine()
    print("Enter description: ")
    val description = StdIn.readLine()
    print("Enter price: ")
    val price = StdIn.readDouble()

    val product = Product(None, name, description, price)
    val result = ProductService.createProduct(product)
    Await.result(result, 5.seconds)
    println("Product created successfully.")
    menu()
  }

  def updateProduct(): Unit = {
    print("Enter product ID to update: ")
    val id = StdIn.readInt()
    print("Enter new name: ")
    val name = StdIn.readLine()
    print("Enter new description: ")
    val description = StdIn.readLine()
    print("Enter new price: ")
    val price = StdIn.readDouble()

    val product = Product(Some(id), name, description, price)
    val result = ProductService.updateProduct(product)
    Await.result(result, 5.seconds)
    println("Product updated successfully.")
    menu()
  }

  def deleteProduct(): Unit = {
    print("Enter product ID to delete: ")
    val id = StdIn.readInt()

    val result = ProductService.deleteProduct(id)
    Await.result(result, 5.seconds)
    println("Product deleted successfully.")
    menu()
  }

  def recordPurchase(): Unit = {
    print("Enter user ID: ")
    val userId = StdIn.readInt()
    print("Enter product ID: ")
    val productId = StdIn.readInt()
    print("Enter quantity: ")
    val quantity = StdIn.readInt()

    val purchase = Purchase(None, userId, productId, quantity)
    val result = PurchaseService.recordPurchase(purchase)
    Await.result(result, 5.seconds)
    println("Purchase recorded successfully.")
    menu()
  }

  def viewPurchaseHistory(): Unit = {
    print("Enter user ID: ")
    val userId = StdIn.readInt()

    val result = PurchaseService.getPurchaseHistory(userId)
    val purchases = Await.result(result, 5.seconds)

    purchases.foreach { p =>
      println(s"Purchase ID: ${p.id.get}, Product ID: ${p.productId}, Quantity: ${p.quantity}")
    }
    menu()
  }

  def getRecommendations(): Unit = {
    print("Enter user ID: ")
    val userId = StdIn.readInt()

    val result = RecommendationService.getRecommendations(userId)
    val products = Await.result(result, 5.seconds)

    if (products.isEmpty) {
      println("No recommendations available.")
    } else {
      println("Recommended Products:")
      products.foreach { p =>
        println(s"Product ID: ${p.id.get}, Name: ${p.name}, Price: ${p.price}")
      }
    }
    menu()
  }

  menu()
}
