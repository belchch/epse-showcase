import { api } from "src/boot/axios"
import { BoqInteriorDoorModel, BoqInteriorDoorUpdateRequest } from "./types"

export const BoqInteriorDoorApi = {
    updateInteriorDoor: (id: number, request: BoqInteriorDoorUpdateRequest, updateVolume: boolean) => {
        return api.put<BoqInteriorDoorModel>(`/api/boq/interior-doors/${id}`, request, {
            params: {
                updateVolume
            }
        })
    },

    createInteriorDoor: (locationId: number) => {
        return api.post<BoqInteriorDoorModel>(`/api/boq/interior-doors`, {}, {
            params: {
                locationId
            }
        })
    },

    deleteInteriorDoor: (id: number) => {
        return api.delete(`/api/boq/interior-doors/${id}`)
    },

    getPhotos: (boqId: number, spotId: number, spotNum: number | undefined) => {
        return api.get<string[]>(`/api/boq/interior-doors/photos`, {
            params: {
                boqId, spotId, spotNum
            }
        })
    }
}