package dev.epse.app.boq.estimate.docx

import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.springframework.stereotype.Service
import dev.epse.app.boq.BoqRepository
import dev.epse.app.boq.estimate.EstimateReportBuilder
import dev.epse.app.boq.estimate.EstimateReportService
import dev.epse.app.common.FileInfo
import dev.epse.app.common.docToByteArray
import dev.epse.app.s3.S3Service

@Service
class EstimateReportDocxService(
    private val s3Service: S3Service,
    private val boqRepository: BoqRepository
) {

    fun build(boqId: Long): FileInfo {
        val doc = XWPFDocument()
        fill(boqId, doc)

        val (fileName, objectKey) = s3Service.uploadDoc(
            prefix = "estimate_report",
            entityId = boqId,
            data = docToByteArray(doc)
        )

        return FileInfo(
            fileName = fileName,
            fileLink = s3Service.generateDownloadUrl(objectKey)
        )
    }

    fun fill(inspectionId: Long, doc: XWPFDocument): Boolean {
        val boq = boqRepository.findByInspectionId(inspectionId)

        if (boq != null) {
            EstimateReportDocxBuilder(
                EstimateReportBuilder().build(boq)
            ).fill(doc)
            return true
        } else {
            return false
        }
    }
}