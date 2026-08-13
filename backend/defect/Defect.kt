package dev.epse.app.defect

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import dev.epse.app.defect.flaw.Flaw
import dev.epse.app.material.Material
import dev.epse.app.standard.Standard
import dev.epse.app.structelem.StructElem

@Entity
@Table(name = "defects")
data class Defect(
    @Id @GeneratedValue
    val id: Long? = null,

    @Column(length = 500, nullable = false)
    var template: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    var standard: Standard,

    @ManyToOne(fetch = FetchType.LAZY)
    var structElem: StructElem,

    @ManyToOne(fetch = FetchType.LAZY)
    var material: Material? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    var flaw: Flaw? = null,

    var hasValue: Boolean = false,
    var hasCause: Boolean = false,

    @Column(nullable = false)
    var isArchived: Boolean
)