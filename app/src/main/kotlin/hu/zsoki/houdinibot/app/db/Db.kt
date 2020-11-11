package hu.zsoki.houdinibot.app.db

object Db {
    val jdbcUrl = System.getProperty("jdbc.database.url") ?: error("You need to define jdbc.database.url property.")

    const val driverClass = "org.sqlite.JDBC"
}
