import { Material } from "src/features/lookup/material/stores/types"
import { StructElem } from "src/features/lookup/struct-elem/stores/types"

export type BoqWindowModel = {
    id: number,
    width: number,
    height: number,
    area: number,
    slopesWidth: number,
    slopesArea: number,
    perimeter: number,
    hasSlopes: boolean,
    replacement: boolean,
    preservation: boolean,
    slopesPainting: boolean,
    material: Material,
    structElems: StructElem[]
}

export type BoqWindowUpdateRequest = {
    id: number,
    width: number,
    height: number,
    hasSlopes: boolean,    
    replacement: boolean,
    preservation: boolean,
    slopesPainting: boolean,
    materialId?: number | undefined
}

export const toWindowUpdateRequest = (window: BoqWindowModel): BoqWindowUpdateRequest => {
    return {
        id: window.id,
        materialId: window.material?.id,
        slopesPainting: window.slopesPainting,
        width: window.width,
        height: window.height,
        hasSlopes: window.hasSlopes,
        replacement: window.replacement,
        preservation: window.preservation,
    }
}