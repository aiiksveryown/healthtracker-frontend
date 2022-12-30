package ie.setu

import ie.setu.config.DbConfig
import ie.setu.config.JavalinConfig

fun main() {
    val dbConnection = DbConfig().getDbConnection()
    JavalinConfig().startJavalinService(dbConnection)
}