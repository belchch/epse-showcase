package dev.epse.app.boq.estimate

import org.springframework.stereotype.Service
import dev.epse.app.boq.Boq
import dev.epse.app.boq.configuration.opening.door.interior.crud.DoorType
import dev.epse.app.boq.estimate.EstimateReportGroupType.*
import dev.epse.app.boq.location.crud.ceilWorks
import dev.epse.app.boq.location.crud.floorWorks
import dev.epse.app.boq.location.crud.supportingWorks
import dev.epse.app.boq.report.description
import dev.epse.app.boq.work.crud.IBoqWork
import dev.epse.app.common.roundHalfUpScale2

@Service
class EstimateReportBuilder {
    fun build(boq: Boq): EstimateReport {
        val groups = mutableListOf<EstimateReportGroup>()

        boq.locations?.forEach { location ->
            location.windows?.forEach {
                groups.add(
                    EstimateReportGroup(
                        type = WINDOW,
                        description = WINDOW.description,
                        works = it.works?.toEstimateReportWorks() ?: emptyList()
                    )
                )
            }

            location.interiorDoors?.forEach {
                val type = when (it.type) {
                    DoorType.INTERIOR -> INTERIOR_DOOR
                    DoorType.ENTRANCE -> ENTRANCE_DOOR
                }

                groups.add(
                    EstimateReportGroup(
                        type = type,
                        description = type.description,
                        works = it.works?.toEstimateReportWorks() ?: emptyList()
                    )
                )
            }

            groups.add(
                EstimateReportGroup(
                    type = FLOOR,
                    description = FLOOR.description,
                    works = location.floorWorks().toEstimateReportWorks())
                )


            location.floor?.sections?.forEach {
                groups.add(
                    EstimateReportGroup(
                        type = FLOOR,
                        description = FLOOR.description,
                        works = (it.works?.toEstimateReportWorks()
                            ?: emptyList())
                    )
                )
            }

            groups.add(
                EstimateReportGroup(
                    type = CEIL,
                    description = CEIL.description,
                    works = location.ceilWorks().toEstimateReportWorks()
                )
            )

            location.ceil?.sections?.forEach {
                groups.add(
                    EstimateReportGroup(
                        type = CEIL,
                        description = CEIL.description,
                        works = (it.works?.toEstimateReportWorks() ?: emptyList())
                    )
                )
            }

            location.wallSections?.forEach {
                groups.add(
                    EstimateReportGroup(
                        type = WALL,
                        description = WALL.description,
                        works = it.works?.toEstimateReportWorks() ?: emptyList()
                    )
                )
            }

            groups.add(
                EstimateReportGroup(
                    type = LOCATION_SUPPORTING,
                    description = "${LOCATION_SUPPORTING.description} ${location.room.name}${location.roomNum?.let { " $it" } ?: ""}",
                    works = location.supportingWorks().toEstimateReportWorks()
                )
            )
        }

        groups.add(
            EstimateReportGroup(
                type = SUPPORTING,
                description = SUPPORTING.description,
                works = boq.works?.toEstimateReportWorks() ?: emptyList()
            )
        )

        return EstimateReport(
            groups = groups.filter { it.works.isNotEmpty() }
        )
    }

    fun List<IBoqWork>.toEstimateReportWorks() = filter { work ->
        work.visible && !work.disabled
    }.map { work ->
        EstimateReportWork(
            name = work.rate.name,
            uom = work.rate.unitOfMeasure.description(),
            volume = work.volume.roundHalfUpScale2(),
            rates = work.rate.sources.map {
                EstimateReportRate(
                    url = it.url,
                    price = it.price
                )
            }
        )
    }
}