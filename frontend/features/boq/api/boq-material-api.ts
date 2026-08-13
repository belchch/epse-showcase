import { api } from "src/boot/axios"
import { BoqRawMaterial, BoqRawMaterialUpdateRequest } from "./types"

export const BoqMaterialApi = {
    getMaterials: (boqId: number) => {
        return api.get<BoqRawMaterial[]>(`/api/boq/raw-materials`, {
            params: {
                boqId
            }
        })
    },

    updateMaterial: (id: number, request: BoqRawMaterialUpdateRequest) => {
        return api.put<BoqRawMaterial>(`/api/boq/raw-materials/${id}`, request)
    }
}