import { api } from "src/boot/axios";
import { BoqWork } from "./types";

export const BoqSupportingWorkApi = {
    getWorks(locationId: number) {
        return api.get<BoqWork[]>(`/api/boq/works/supporting`, {
            params: {
                locationId
            }
        })
    }
}