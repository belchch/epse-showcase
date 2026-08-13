import { Spot } from 'src/features/lookup/spot/stores/types'
import { Rate, UnitOfMeasure } from 'src/features/rate/stores/types'
import { BoqCeilModel } from './ceil/types'
import { BoqFloorModel } from './floor/types'
import { BoqInteriorDoorModel } from './interior-door/types'
import { BoqWallSectionModel } from './wall/types'
import { BoqWindowModel } from './window/types'
import { BoqFixedAssetModel } from './fixed-asset/types'

export type BoqLocation = {
  id: number
  room: Spot
  boqId: number
  roomNum?: number
  area?: number
  perimeter?: number
  height?: number
  floor: BoqFloorModel
  ceil: BoqCeilModel
  interiorDoors: BoqInteriorDoorModel[]
  windows: BoqWindowModel[]
  wallSections: BoqWallSectionModel[]
  fixedAssets: BoqFixedAssetModel[]
}

export type Boq = {
  id: number
}

export type BoqLocationUpdateRequest = {
  id: number
  roomId: number
  roomNum?: number | undefined
  area?: number | undefined
  perimeter?: number | undefined
  height?: number | undefined
}

export const toLocationUpdateRequest = (location: BoqLocation) => {
  return {
    id: location.id,
    roomId: location.room.id!,
    roomNum: location.roomNum,
    boqId: location.boqId,
    area: location.area,
    perimeter: location.perimeter,
    height: location.height,
  }
}

export type BoqWork = {
  id: number
  locationId: number
  roomName: string
  roomNum?: number
  rate: Rate
  volume: number
  disabled: boolean
  visible: boolean
  mountType: WorkMountType
  calculatedVolume: number
}

export type BoqRawMaterial = {
  id: number
  rawMaterial: RawMaterial
  boqId: number
  volume: number
  calculatedVolume: number
  disabled: boolean
  visible: boolean
  workId: number
  work: BoqWork
}

export type BoqRawMaterialUpdateRequest = {
  volume: number
  disabled: boolean
}

export type RawMaterial = {
  id: number
  name: string
  unitOfMeasure: UnitOfMeasure
}

export type BoqWorkUpdateRequest = {
  id: number
  volume: number
  disabled: boolean
  visible: boolean
  mountType: WorkMountType
}

export const toMaterialUpdateRequest = (material: BoqRawMaterial): BoqRawMaterialUpdateRequest => {
  return {
    volume: material.volume,
    disabled: material.disabled,
  }
}

export const toWorkUpdateRequest = (work: BoqWork): BoqWorkUpdateRequest => {
  return {
    id: work.id,
    volume: work.volume,
    disabled: work.disabled,
    visible: work.visible,
    mountType: work.mountType,
  }
}

export type WorkMountType =
  | 'LOCATION'
  | 'CEIL_SECTION'
  | 'FLOOR_SECTION'
  | 'WALL_SECTION'
  | 'DOOR'
  | 'WINDOW'
  | 'BOQ'
