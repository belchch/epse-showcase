package dev.epse.app.defect

import jakarta.persistence.EntityNotFoundException
import dev.epse.app.defect.flaw.FlawRepository
import dev.epse.app.defect.flaw.FlawResponse
import dev.epse.app.defect.flaw.toResponse
import dev.epse.app.material.MaterialRepository
import dev.epse.app.material.MaterialResponse
import dev.epse.app.material.toResponse
import dev.epse.app.standard.StandardRepository
import dev.epse.app.standard.StandardResponse
import dev.epse.app.standard.toResponse
import dev.epse.app.structelem.StructElemRepository
import dev.epse.app.structelem.StructElemResponse
import dev.epse.app.structelem.toResponse

data class DefectUpdateRequest(
    var id: Long?,
    val template: String,
    val standardId: Long,
    val structElemId: Long,
    val materialId: Long?,
    var flawId: Long?,
    var hasValue: Boolean,
    var hasCause: Boolean,
)

data class DefectResponse(
    val id: Long?,
    val template: String,
    val standard: StandardResponse,
    val structElem: StructElemResponse,
    val material: MaterialResponse?,
    var flaw: FlawResponse?,
    var hasValue: Boolean,
    var hasCause: Boolean,
    val isArchived: Boolean
)

fun DefectUpdateRequest.toEntity(
    structElemRepository: StructElemRepository,
    materialRepository: MaterialRepository,
    flawRepository: FlawRepository,
    standardRepository: StandardRepository,
    original: Defect?
): Defect {
    return Defect(
        id = id,
        template = template,
        standard = standardRepository.findById(standardId)
            .orElseThrow { EntityNotFoundException("Standard not found with id: $standardId") },
        structElem = structElemRepository.findById(structElemId)
            .orElseThrow { EntityNotFoundException("StructElem not found with id: $structElemId") },
        material = materialId?.let {
            materialRepository.findById(it)
                .orElseThrow { EntityNotFoundException("Material not found with id: $materialId") }
        },
        flaw = flawId?.let {
            flawRepository.findById(it)
                .orElseThrow { EntityNotFoundException("Flaw not found with id: $flawId") }
        },
        hasCause = hasCause,
        hasValue = hasValue,
        isArchived = original?.isArchived ?: false
    )
}

fun Defect.toResponse() = DefectResponse(
    id = id!!,
    template = template,
    standard = standard.toResponse(),
    structElem = structElem.toResponse(),
    material = material?.toResponse(),
    flaw = flaw?.toResponse(),
    hasValue = hasValue,
    hasCause = hasCause,
    isArchived = isArchived
)