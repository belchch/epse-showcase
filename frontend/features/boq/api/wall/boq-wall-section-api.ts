import { api } from "src/boot/axios"
import { BoqWallSectionModel, BoqWallSectionUpdateRequest } from "./types"

export const BoqWallSectionApi = {
    updateWallSection: (id: number, request: BoqWallSectionUpdateRequest, updateVolume: boolean) => {
        return api.put<BoqWallSectionModel>(`/api/boq/wall-sections/${id}`, request, {
            params: {
                updateVolume
            }
        })
    },

    createWallSection: (locationId: number) => {
        return api.post<BoqWallSectionModel>(`/api/boq/wall-sections`, {}, {
            params: {
                locationId
            }
        })
    },

    deleteWallSection: (id: number) => {
        return api.delete(`/api/boq/wall-sections/${id}`)
    },

    getPhotos: (boqId: number, spotId: number, spotNum: number | undefined) => {
        return api.get<string[]>(`/api/boq/wall-sections/photos`, {
            params: {
                boqId, spotId, spotNum
            }
        })
    }
}