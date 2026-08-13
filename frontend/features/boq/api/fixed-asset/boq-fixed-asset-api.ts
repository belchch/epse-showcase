import { api } from "src/boot/axios"
import { BoqFixedAssetModel, BoqFixedAssetUpdateRequest } from "./types"

export const BoqFixedAssetApi = {
    updateFixedAsset: (id: number, request: BoqFixedAssetUpdateRequest, updateVolume: boolean) => {
        return api.put<BoqFixedAssetModel>(`/api/boq/fixed-assets/${id}`, request, {
            params: {
                updateVolume
            }
        })
    },

    createFixedAsset: (locationId: number) => {
        return api.post<BoqFixedAssetModel>(`/api/boq/fixed-assets`, {}, {
            params: {
                locationId
            }
        })
    },

    deleteFixedAsset: (id: number) => {
        return api.delete(`/api/boq/fixed-assets/${id}`)
    },

    getPhotos: (boqId: number, spotId: number, spotNum: number | undefined) => {
        return api.get<string[]>(`/api/boq/fixed-assets/photos`, {
            params: {
                boqId, spotId, spotNum
            }
        })
    }
}