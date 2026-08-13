package dev.epse.app.defect.search

import org.springframework.stereotype.Service
import dev.epse.app.defect.DefectRepository
import dev.epse.app.defect.flaw.toResponse
import dev.epse.app.defect.toResponse
import dev.epse.app.material.Material
import dev.epse.app.material.MaterialRepository
import dev.epse.app.material.toResponse
import dev.epse.app.structelem.toResponse

@Service
class DefectSearchService(
    private val defectRepository: DefectRepository,
    private val materialRepository: MaterialRepository
) {
    fun search(request: DefectSearchRequest): DefectSearchResponse {
        fun searchLocal(materialId: Long? = null) = defectRepository.findAll(searchSpec(
            structElemId = request.structElemId,
            materialId = materialId,
            flawId = request.flawId,
            defectId = request.defectId
        ))

        var defects = searchLocal(request.materialId)

        if (defects.isEmpty()) {
           defects = searchLocal(null)
        }

        val material = request.materialId?.let {
            materialRepository.findById(it).get()
        }

        val structElems = defects.map { it.structElem }.distinct()
        val structElemMaterials = structElems.flatMap { it.materials }.distinct()
        val materialsResult = if (material != null) listOf(material) else structElemMaterials

        return DefectSearchResponse(
            structElems = defects.map { it.structElem }.distinct().map { it.toResponse() },
            materials = materialsResult.map { it.toResponse() },
            flaws = defects.mapNotNull { it.flaw }.distinct().map { it.toResponse() },
            defects = defects.map { it.toResponse() }
        )
    }


}