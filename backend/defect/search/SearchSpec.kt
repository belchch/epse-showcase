package dev.epse.app.defect.search

import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import dev.epse.app.defect.Defect
import dev.epse.app.defect.flaw.Flaw
import dev.epse.app.material.Material
import dev.epse.app.structelem.StructElem

fun searchSpec(
    structElemId: Long?,
    materialId: Long?,
    flawId: Long?,
    defectId: Long?
): Specification<Defect> {
    return Specification { root, query, cb ->
        val predicates = mutableListOf<Predicate>()

        structElemId?.let {
            predicates.add(cb.equal(root.get<StructElem>("structElem").get<Long>("id"), it))
        }
        materialId?.let {
            predicates.add(cb.equal(root.get<Material>("material").get<Long>("id"), it))
        }
        flawId?.let {
            predicates.add(cb.equal(root.get<Flaw>("flaw").get<Long>("id"), it))
        }
        defectId?.let {
            predicates.add(cb.equal(root.get<Long>("id"), it))
        }

        cb.and(*predicates.toTypedArray())
    }
}