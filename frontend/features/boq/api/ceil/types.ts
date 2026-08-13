import { Material } from "src/features/lookup/material/stores/types"
import { StructElem } from "src/features/lookup/struct-elem/stores/types"

export type BoqCeilModel = {
    id: number,
    moldingLength: number,
    moldingReplacement: boolean,
    moldingPreservation: boolean,
    moldingMaterial?: Material,
    sections: BoqCeilSectionModel[],
    structElems: StructElem[]
}

export type BoqCeilSectionModel = {
    id: number,
    material: Material,
    area: number,
    materialReplacement: boolean,
    materialPreservation: boolean,
    antisepticTreatment: boolean
}

export type BoqCeilUpdateRequest = {
    id: number,
    moldingLength: number,
    moldingReplacement: boolean,
    moldingPreservation: boolean,
    moldingMaterialId: number | undefined
}

export type BoqCeilSectionUpdateRequest = {
    id: number,
    materialId?: number | undefined,
    area: number,
    materialReplacement: boolean,
    materialPreservation: boolean,
    antisepticTreatment: boolean
}

export const toCeilUpdateRequest = (boqCeil: BoqCeilModel): BoqCeilUpdateRequest => {
    return {
        id: boqCeil.id,
        moldingLength: boqCeil.moldingLength,
        moldingReplacement: boqCeil.moldingReplacement,
        moldingPreservation: boqCeil.moldingPreservation,
        moldingMaterialId: boqCeil.moldingMaterial?.id
    }
}

export const toCeilSectionUpdateRequest = (ceilSection: BoqCeilSectionModel): BoqCeilSectionUpdateRequest => {
    return {
        id: ceilSection.id,
        materialId: ceilSection.material.id,
        area: ceilSection.area,
        materialReplacement: ceilSection.materialReplacement,
        materialPreservation: ceilSection.materialPreservation,
        antisepticTreatment: ceilSection.antisepticTreatment        
    }
}