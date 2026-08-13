import { StructElem } from "src/features/lookup/struct-elem/stores/types"

export type BoqInteriorDoorModel = {
    id: number,
    width: number,
    height: number,
    area: number,
    slopesWidth: number,
    slopesArea: number,
    perimeter: number,
    hasSlopes: boolean,
    trimsReplacement: boolean,
    trimsPreservation: boolean,
    replacement: boolean,
    preservation: boolean,
    type: DoorType,
    structElems: StructElem[]
}

export type BoqInteriorDoorUpdateRequest = {
    id: number,
    width: number,
    height: number,
    hasSlopes: boolean,
    trimsReplacement: boolean,
    trimsPreservation: boolean,
    replacement: boolean,
    preservation: boolean,
    type: DoorType
}

export const toInteriorDoorUpdateRequest = (interiorDoor: BoqInteriorDoorModel): BoqInteriorDoorUpdateRequest => {
    return {
        id: interiorDoor.id,
        width: interiorDoor.width,
        height: interiorDoor.height,
        hasSlopes: interiorDoor.hasSlopes,
        trimsReplacement: interiorDoor.trimsReplacement,
        trimsPreservation: interiorDoor.trimsPreservation,
        replacement: interiorDoor.replacement,
        preservation: interiorDoor.preservation,
        type: interiorDoor.type
    }
}

export type DoorType = 'INTERIOR' | 'ENTRANCE'