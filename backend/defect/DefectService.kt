package dev.epse.app.defect

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import dev.epse.app.defect.flaw.FlawRepository
import dev.epse.app.material.MaterialRepository
import dev.epse.app.standard.StandardRepository
import dev.epse.app.structelem.StructElemRepository
import kotlin.jvm.optionals.getOrNull

@Service
class DefectService(
    private val defectRepository: DefectRepository,
    private val structElemRepository: StructElemRepository,
    private val materialRepository: MaterialRepository,
    private val flawRepository: FlawRepository,
    private val standardRepository: StandardRepository,
) {

    fun createDefect(request: DefectUpdateRequest): DefectResponse {
        return defectRepository.save(request.toEntity(
            structElemRepository = structElemRepository,
            materialRepository = materialRepository,
            flawRepository = flawRepository,
            standardRepository = standardRepository,
            original = null
        )).toResponse()
    }

    fun getDefect(id: Long): DefectResponse {
        return defectRepository.findById(id).map { it.toResponse() }
            .orElseThrow { notFoundException(id) }
    }

    fun getAllDefects(all: Boolean): List<DefectResponse> {
        return defectRepository.findAll()
            .filter { all || !it.isArchived }
            .map { it.toResponse() }
            .sortedBy { it.template }
    }

    fun updateDefect(
        id: Long,
        request: DefectUpdateRequest
    ): DefectResponse {
        val original = defectRepository.findById(id).getOrNull()

        if (original == null) {
            throw notFoundException(id)
        }

        request.id = id

        return defectRepository.save(request.toEntity(
            structElemRepository = structElemRepository,
            materialRepository = materialRepository,
            flawRepository = flawRepository,
            standardRepository = standardRepository,
            original = original
        )).toResponse()
    }

    fun delete(id: Long) {
        changeIsArchived(id, true)
    }

    fun restore(id: Long) {
        changeIsArchived(id, false)
    }

    private fun changeIsArchived(id: Long, value: Boolean) {
        val row = defectRepository.findById(id).orElseThrow { notFoundException(id) }
        row.isArchived = value
        defectRepository.save(row)
    }
}

private fun notFoundException(id: Long) = EntityNotFoundException("Defect not found with id: $id")