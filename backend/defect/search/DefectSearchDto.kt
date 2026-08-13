package dev.epse.app.defect.search

import dev.epse.app.defect.DefectResponse
import dev.epse.app.defect.flaw.FlawResponse
import dev.epse.app.material.MaterialResponse
import dev.epse.app.structelem.StructElemResponse

class DefectSearchRequest(
    val structElemId: Long?,
    val materialId: Long?,
    val flawId: Long?,
    val defectId: Long?,
)

class DefectSearchResponse(
    val structElems: List<StructElemResponse>,
    val materials: List<MaterialResponse>,
    val flaws: List<FlawResponse>,
    val defects: List<DefectResponse>
)