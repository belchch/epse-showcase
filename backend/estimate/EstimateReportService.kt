package dev.epse.app.boq.estimate

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import dev.epse.app.boq.BoqRepository

@Service
class EstimateReportService(
    private val estimateReportBuilder: EstimateReportBuilder,
    private val boqRepository: BoqRepository
) {
    fun buildReport(inspectionId: Long): EstimateReport {
        val boq = boqRepository.findByInspectionId(inspectionId)

        if (boq == null) {
            throw EntityNotFoundException("Boq not found for inspection with id: $inspectionId")
        }

        return estimateReportBuilder.build(boq)
    }
}