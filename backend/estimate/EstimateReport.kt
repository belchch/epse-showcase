package dev.epse.app.boq.estimate

import java.math.BigDecimal
import java.math.RoundingMode

data class EstimateReport(
    val groups: List<EstimateReportGroup>
) {
    val worksTotal = groups.sumOf { it.total }
    private val unexpectedExpensesFactor = BigDecimal(0.02)
    private val transportationFactor = BigDecimal(0.05)
    private val garbageRemovalFactor = BigDecimal(0.05)

    val unexpectedExpenses: BigDecimal = (worksTotal * unexpectedExpensesFactor).setScale(2, RoundingMode.HALF_UP)
    val garbageRemoval: BigDecimal = (worksTotal * garbageRemovalFactor).setScale(2, RoundingMode.HALF_UP)
    val transportation: BigDecimal = (worksTotal * transportationFactor).setScale(2, RoundingMode.HALF_UP)
    val total = worksTotal + unexpectedExpenses + garbageRemoval + transportation
}

data class EstimateReportGroup(
    val type: EstimateReportGroupType,
    val description: String,
    val works: List<EstimateReportWork>
) {
    val total: BigDecimal = works.sumOf { it.averageCost }.setScale(2, RoundingMode.HALF_UP)
}

data class EstimateReportWork(
    val name: String,
    val uom: String,
    val volume: Double,
    val rates: List<EstimateReportRate>,
) {
    val averagePrice: BigDecimal = if (rates.isNotEmpty()) {
        rates.map { it.price }.average()
    } else {
        0.0
    }.toBigDecimal().setScale(2, RoundingMode.HALF_UP)

    val averageCost: BigDecimal = (averagePrice * volume.toBigDecimal()).setScale(2, RoundingMode.HALF_UP)
}

data class EstimateReportRate(
    val url: String,
    val price: Double
)

enum class EstimateReportGroupType(val description: String) {
    WINDOW ("Окна"),
    INTERIOR_DOOR ("Межкомнатные двери"),
    ENTRANCE_DOOR ("Входные двери"),
    FLOOR ("Полы"),
    WALL ("Стены"),
    CEIL ("Потолки"),
    LOCATION_SUPPORTING ("Дополнительные работы по помещению: "),
    SUPPORTING ("ДОПОЛНИТЕЛЬНЫЕ РАБОТЫ")
}
