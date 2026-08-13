import { Material } from "src/features/lookup/material/stores/types"
import { StructElem } from "src/features/lookup/struct-elem/stores/types"

export type BoqFixedAssetModel = {
    id: number,
    name: string,
    height: number,
    perimeter: number,
    surfaceArea: number,
    material: Material,
    replacement: boolean,
    painting: boolean,
    plaster: boolean,
    structElems: StructElem[],
    width?: number,
    length?: number,
    area?: number,
    hasCustomShape: boolean,
    type: FixedAssetType
}

export type FixedAssetType = 'COLUMN' | 'STAIRWAY'

export type BoqFixedAssetUpdateRequest = {
    id: number,    
    material: Material,
    replacement: boolean,
    painting: boolean,
    plaster: boolean
}

export const toFixedAssetUpdateRequest = (fixedAsset: BoqFixedAssetModel): BoqFixedAssetUpdateRequest => {
    return {
        id: fixedAsset.id,        
        material: fixedAsset.material,
        replacement: fixedAsset.replacement,
        painting: fixedAsset.painting,
        plaster: fixedAsset.plaster
    }
}