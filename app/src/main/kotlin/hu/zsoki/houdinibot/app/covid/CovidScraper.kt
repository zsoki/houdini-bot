package hu.zsoki.houdinibot.app.covid

import org.jsoup.Jsoup

object CovidScraper {
    private const val url = "https://koronavirus.gov.hu/"

    fun scrapeData(): CovidStatsScrapeResult {
        try {
            val document = Jsoup.connect(url).get()

            }
            return CovidStatsScrapeResult.Success(covidStats)
        } catch (ioException: IOException) {
            return CovidStatsScrapeResult.Error
        }
    }
}

sealed class CovidStatsScrapeResult {
    object Error : CovidStatsScrapeResult()
    class Success(val covidStats: CovidStats) : CovidStatsScrapeResult()
}