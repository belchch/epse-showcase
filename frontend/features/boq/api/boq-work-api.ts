import { api } from "src/boot/axios"
import { BoqWork, BoqWorkUpdateRequest } from "./types"

export const BoqWorkApi = {
    buildWorks: (boqId: number, locationId: number) => {
        return api.post(`/api/boq/works/build`, {}, {
            params: {
                boqId,
                locationId
            }
        })
    },

    getWorks: (boqId: number) => {
        return api.get<BoqWork[]>(`/api/boq/works`, {
            params: {
                boqId
            }
        })
    },

    updateWork: (id: number, request: BoqWorkUpdateRequest, changeValue: boolean) => {
        return api.put<BoqWork>(`/api/boq/works/${id}`, request, {
            params: {
                changeValue
            }
        })
    }
}