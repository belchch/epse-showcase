package dev.epse.app.boq.estimate

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/estimate-report")
class EstimateReportController(
    private val estimateReportService: EstimateReportService
) {
    @GetMapping
    fun getReport(@RequestParam inspectionId: Long): EstimateReport {
        return estimateReportService.buildReport(inspectionId)
    }
}