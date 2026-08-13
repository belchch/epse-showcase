import { BoqCeilModel, BoqCeilSectionModel, BoqCeilSectionUpdateRequest, BoqCeilUpdateRequest } from "./types"
import { api } from "src/boot/axios"

export const BoqCeilApi = {
    updateCeil: (id: number, request: BoqCeilUpdateRequest, updateVolume: boolean) => {
        return api.put<BoqCeilModel>(`/api/boq/ceil/${id}`, request, {
            params: {
                updateVolume
            }
        })
    },

    updateCeilSection: (id: number, request: BoqCeilSectionUpdateRequest, updateVolume: boolean) => {
        return api.put<BoqCeilSectionModel>(`/api/boq/ceil-sections/${id}`, request, {
            params: {
                updateVolume
            }
        })
    },

    createCeilSection: (ceilId: number) => {
        return api.post<BoqCeilSectionModel>(`/api/boq/ceil-sections`, {}, {
            params: {
                ceilId
            }
        })
    },

    deleteCeilSection: (id: number) => {
        return api.delete(`/api/boq/ceil-sections/${id}`)
    },

    getPhotos: (boqId: number, spotId: number, spotNum: number | undefined) => {
        return api.get<string[]>(`/api/boq/ceil/photos`, {
            params: {
                boqId, spotId, spotNum
            }
        })
    }
}