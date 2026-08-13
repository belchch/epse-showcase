import { api } from "src/boot/axios"
import { BoqWindowModel, BoqWindowUpdateRequest } from "./types"

export const BoqWindowApi = {
    updateWindow: (id: number, request: BoqWindowUpdateRequest, updateVolume: boolean) => {
        return api.put<BoqWindowModel>(`/api/boq/windows/${id}`, request, {
            params: {
                updateVolume
            }
        })
    },

    createWindow: (locationId: number) => {
        return api.post<BoqWindowModel>(`/api/boq/windows`, {}, {
            params: {
                locationId
            }
        })
    },

    deleteWindow: (id: number) => {
        return api.delete(`/api/boq/windows/${id}`)
    },

    getPhotos: (boqId: number, spotId: number, spotNum: number | undefined) => {
        return api.get<string[]>(`/api/boq/windows/photos`, {
            params: {
                boqId, spotId, spotNum
            }
        })
    }
}