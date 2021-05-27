package hu.zsoki.houdinibot.app.covid

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime

object HungaryCovidTable : Table("covid_hun") {
    val lastUpdated: Column<LocalDateTime> = datetime("last_updated")
}

object CovidRegionUpdate : Table("region_update") {
    val id: Column<Long> = long("id")

}

object WorldCovidTable : Table("covid_world")

object PoliceMeasuresTable : Table("covid_police")
