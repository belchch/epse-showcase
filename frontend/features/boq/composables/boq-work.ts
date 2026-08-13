import { storeToRefs } from 'pinia'
import { useBoqWorkStore } from '../stores/boq-work-store'
import { BoqWorkApi } from '../api/boq-work-api'
import { useBoqStore } from '../stores/boq-store'
import _ from 'lodash'
import { BoqWork, toWorkUpdateRequest } from '../api/types'
import { useBoqRawMaterialService } from './boq-material'

export const useBoqWorkService = () => {
  const { works, fetchingWorks } = storeToRefs(useBoqWorkStore())
  const { boq } = storeToRefs(useBoqStore())
  const { requestMaterials } = useBoqRawMaterialService()

  const requestWorks = async () => {
    fetchingWorks.value = true
    const response = await BoqWorkApi.getWorks(boq.value!.id)
    works.value = response.data
    fetchingWorks.value = false
    requestMaterials().catch((error) => {
      console.error('Error fetching materials:', error)
    })
  }

  const updateWork = async (work: BoqWork, changeValue: boolean) => {
    await BoqWorkApi.updateWork(work.id, toWorkUpdateRequest(work), changeValue)
    await requestWorks()
  }

  return {
    requestWorks,
    updateWork,
  }
}
