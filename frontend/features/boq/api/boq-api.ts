import { api } from "src/boot/axios"
import { Boq, BoqLocation, BoqLocationUpdateRequest } from "./types"
import { FileInfo } from "src/common/types"

export const BoqApi = {
    buildBoq: (inspectionId: number) => {
        return api.post(`/api/boq/build`, {}, {
            params: {
                inspectionId
            }
        })
    },

    getBoq: (inspectionId: number) => {
        return api.get<Boq>(`/api/boq`, {
            params: {
                inspectionId
            }
        })
    },

    getLocations: (boqId: number) => {
        return api.get<BoqLocation[]>(`/api/boq/locations`, {
            params: {
                boqId
            }
        })
    },

    updateLocation: (id: number, location: BoqLocationUpdateRequest) => {
        return api.put<BoqLocation>(`/api/boq/locations/${id}`, location)
    },

    deleteLocation: (id: number) => {
        return api.delete(`/api/boq/locations/${id}`)
    },

    buildReport: (inspectionId: number) => {
        return api.post<FileInfo>(`/api/boq-report/build-docx`, {}, {
            params: {
                inspectionId
            }
        })
    },

    buildEstimateReport: (inspectionId: number) => {
        return api.post<FileInfo>(`/api/stock-report/build-docx`, {}, {
            params: {
                inspectionId
            }
        })
    }
}