import { storeToRefs } from "pinia"
import { BoqCommonWorkApi } from "../api/boq-common-work-api"
import { useBoqStore } from "../stores/boq-store"
import { BoqWork, toWorkUpdateRequest } from "../api/types"

export const useBoqCommonWorkService = () => {
    const { boq } = storeToRefs(useBoqStore())

    const requestWorks = async () => {
        const response = await BoqCommonWorkApi.getCommonWorks(boq.value!.id);
        return response.data;
    }

    const updateWork = async (work: BoqWork) => {
        const response = await BoqCommonWorkApi.updateWork(work.id, toWorkUpdateRequest(work))
        return response.data;
    }

    return {
        requestWorks,
        updateWork
    }
}
