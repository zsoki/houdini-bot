package hu.zsoki.houdinibot.app.db

private val jdbcUrlEnvVariable = System.getenv("JDBC_DATABASE_URL") ?: error("You need to define JDBC_DATABASE_URL environment variable.")

// TODO extra does work - Auth issue
// private val extraJdbcProperties = mapOf("reWriteBatchedInserts" to true)
//val jdbcUrl = jdbcUrlEnvVariable + extraJdbcProperties.toJdbcParams()

val jdbcUrl = jdbcUrlEnvVariable
const val driverClass = "org.postgresql.Driver"

private fun Map<String, Any>.toJdbcParams() = this.map { "&${it.key}=${it.value}" }
