package dev.epse.app.boq.estimate.docx

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import dev.epse.app.common.FileInfo

@RestController
@RequestMapping("/api/estimate-report/build-docx")
class EstimateReportDocxController(
    private val estimateReportReportDocxService: EstimateReportDocxService
) {
    @PostMapping
    fun buildDocx(
        @RequestParam inspectionId: Long
    ): ResponseEntity<FileInfo> {
        val response = estimateReportReportDocxService.build(inspectionId)
        return ResponseEntity.ok(response)
    }
}