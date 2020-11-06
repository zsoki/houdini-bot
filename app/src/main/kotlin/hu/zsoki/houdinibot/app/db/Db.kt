package hu.zsoki.houdinibot.app.db

private val jdbcUrlEnvVariable = System.getenv("JDBC_DATABASE_URL") ?: error("You need to define JDBC_DATABASE_URL environment variable.")

val jdbcUrl = jdbcUrlEnvVariable
const val driverClass = "org.sqlite.JDBC"
