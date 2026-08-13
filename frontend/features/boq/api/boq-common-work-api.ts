import { api } from "src/boot/axios";
import { BoqWork, BoqWorkUpdateRequest } from "./types";

export const BoqCommonWorkApi = {
    getCommonWorks(boqId: number) {
        return api.get<BoqWork[]>(`/api/boq/works/common`, {
            params: {
                boqId
            }
        })
    },
    updateWork: (id: number, request: BoqWorkUpdateRequest) => {
        return api.put<BoqWork>(`/api/boq/works/common/${id}`, request)
    }
}
