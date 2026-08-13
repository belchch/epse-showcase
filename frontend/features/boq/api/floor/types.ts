import { Material } from "src/features/lookup/material/stores/types"
import { StructElem } from "src/features/lookup/struct-elem/stores/types"

export type BoqFloorModel = {
    id: number,
    baseboardLength: number,
    baseboardReplacement: boolean,
    baseboardPreservation: boolean,
    baseboardMaterial?: Material,
    sections: BoqFloorSectionModel[],
    structElems: StructElem[]
}

export type BoqFloorSectionModel = {
    id: number,
    material: Material,
    area: number,
    materialReplacement: boolean,
    materialPreservation: boolean,
    screedLeveling: boolean,
    antisepticTreatment: boolean
}

export type BoqFloorUpdateRequest = {
    id: number,
    baseboardLength: number,
    baseboardReplacement: boolean,
    baseboardPreservation: boolean,
    baseboardMaterialId: number | undefined
}

export type BoqFloorSectionUpdateRequest = {
    id: number,
    materialId?: number | undefined,
    area: number,
    materialReplacement: boolean,
    materialPreservation: boolean,
    antisepticTreatment: boolean,
    screedLeveling: boolean
}

export const toFloorUpdateRequest = (boqFloor: BoqFloorModel): BoqFloorUpdateRequest => {
    return {
        id: boqFloor.id,
        baseboardLength: boqFloor.baseboardLength,
        baseboardReplacement: boqFloor.baseboardReplacement,
        baseboardPreservation: boqFloor.baseboardPreservation,
        baseboardMaterialId: boqFloor.baseboardMaterial?.id
    }
}

export const toFloorSectionUpdateRequest = (floorSection: BoqFloorSectionModel): BoqFloorSectionUpdateRequest => {
    return {
        id: floorSection.id,
        materialId: floorSection.material?.id,
        area: floorSection.area,
        materialReplacement: floorSection.materialReplacement,
        materialPreservation: floorSection.materialPreservation,
        screedLeveling: floorSection.screedLeveling,
        antisepticTreatment: floorSection.antisepticTreatment
    }
}