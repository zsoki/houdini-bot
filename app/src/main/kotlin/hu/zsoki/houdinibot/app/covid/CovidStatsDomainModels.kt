package hu.zsoki.houdinibot.app.covid

import java.time.LocalDateTime

inline fun covidStats(block: CovidStats.Builder.() -> Unit): CovidStats {
    return CovidStats.Builder().also(block).build()
}

data class CovidStats(
    val hungaryCovidStats: HungaryCovidStats,
    val worldCovidStats: WorldCovidStats,
    val policeMeasuresStats: PoliceMeasuresStats
) {
    class Builder {
        var hungaryCovidStats: HungaryCovidStats? = null
        var worldCovidStats: WorldCovidStats? = null
        var policeMeasuresStats: PoliceMeasuresStats? = null

        inline fun hungaryCovidStats(block: HungaryCovidStats.Builder.() -> Unit) {
            hungaryCovidStats = HungaryCovidStats.Builder().apply(block).build()
        }

        inline fun worldCovidStats(block: WorldCovidStats.Builder.() -> Unit) {
            worldCovidStats = WorldCovidStats.Builder().apply(block).build()
        }

        inline fun policeMeasuresStats(block: PoliceMeasuresStats.Builder.() -> Unit) {
            policeMeasuresStats = PoliceMeasuresStats.Builder().apply(block).build()
        }

        fun build() = CovidStats(
            checkNotNull(hungaryCovidStats),
            checkNotNull(worldCovidStats),
            checkNotNull(policeMeasuresStats)
        )
    }
}

data class HungaryCovidStats(
    val lastUpdated: LocalDateTime,
    val infectedByRegion: Map<String, Int>,
    val recoveredByRegion: Map<String, Int>,
    val deadByRegion: Map<String, Int>,
    val quarantined: Int,
    val sampled: Int
) {
    val infected = infectedByRegion.values.sum()
    val recovered = recoveredByRegion.values.sum()
    val dead = deadByRegion.values.sum()

    class Builder {
        private var lastUpdated: LocalDateTime? = null
        var infectedByRegion: Map<String, Int>? = null
        var recoveredByRegion: Map<String, Int>? = null
        var deadByRegion: Map<String, Int>? = null
        var quarantined: Int? = null
        var sampled: Int? = null

        fun lastUpdated(block: () -> String) {

        }

        fun build() = HungaryCovidStats(
            checkNotNull(lastUpdated),
            checkNotNull(infectedByRegion),
            checkNotNull(recoveredByRegion),
            checkNotNull(deadByRegion),
            checkNotNull(quarantined),
            checkNotNull(sampled)
        )
    }
}

data class WorldCovidStats(
    val lastUpdated: LocalDateTime,
    val infected: Int,
    val recovered: Int,
    val dead: Int
) {
    class Builder {
        var lastUpdated: LocalDateTime? = null
        var infected: Int? = null
        var recovered: Int? = null
        var dead: Int? = null

        fun build() = WorldCovidStats(
            checkNotNull(lastUpdated),
            checkNotNull(infected),
            checkNotNull(recovered),
            checkNotNull(dead)
        )
    }
}

data class PoliceMeasuresStats(
    val lastUpdated: LocalDateTime,
    val curfewPoliceMeasuresBySeverity: Map<String, Int>,
    val quarantinePoliceMeasuresBySeverity: Map<String, Int>,
    val maskPoliceMeasuresBySeverity: Map<String, Int>,
    val openingHoursPoliceMeasuresBySeverity: Map<String, Int>,
    val travelPoliceMeasuresBySeverity: Map<String, Int>,
    val merchantPoliceMeasuresBySeverity: Map<String, Int>
) {
    val curfewPoliceMeasures = curfewPoliceMeasuresBySeverity.values.sum()
    val quarantinePoliceMeasures = quarantinePoliceMeasuresBySeverity.values.sum()
    val maskPoliceMeasures = maskPoliceMeasuresBySeverity.values.sum()
    val openingHoursPoliceMeasures = openingHoursPoliceMeasuresBySeverity.values.sum()
    val travelPoliceMeasures = travelPoliceMeasuresBySeverity.values.sum()
    val merchantPoliceMeasures = merchantPoliceMeasuresBySeverity.values.sum()

    class Builder {
        var lastUpdated: LocalDateTime? = null
        var curfewPoliceMeasuresBySeverity: Map<String, Int>? = null
        var quarantinePoliceMeasuresBySeverity: Map<String, Int>? = null
        var maskPoliceMeasuresBySeverity: Map<String, Int>? = null
        var openingHoursPoliceMeasuresBySeverity: Map<String, Int>? = null
        var travelPoliceMeasuresBySeverity: Map<String, Int>? = null
        var merchantPoliceMeasuresBySeverity: Map<String, Int>? = null

        fun build() = PoliceMeasuresStats(
            checkNotNull(lastUpdated),
            checkNotNull(curfewPoliceMeasuresBySeverity),
            checkNotNull(quarantinePoliceMeasuresBySeverity),
            checkNotNull(maskPoliceMeasuresBySeverity),
            checkNotNull(openingHoursPoliceMeasuresBySeverity),
            checkNotNull(travelPoliceMeasuresBySeverity),
            checkNotNull(merchantPoliceMeasuresBySeverity)
        )
    }
}