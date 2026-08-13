import { storeToRefs } from 'pinia'
import { useBoqStore } from '../stores/boq-store'
import { useInspectionsStore } from 'src/features/inspection/store/inspection-store'
import { BoqApi } from '../api/boq-api'
import { BoqLocation, toLocationUpdateRequest } from '../api/types'
import { useBoqWorkService } from './boq-work'
import { useBoqRawMaterialService } from './boq-material'

export const useBoqService = () => {
  const { boq, locations, initialized, fetchingLocations } = storeToRefs(useBoqStore())
  const { selectedInspectionId } = storeToRefs(useInspectionsStore())
  const { requestWorks } = useBoqWorkService()

  const requestBoq = async () => {
    initialized.value = false
    fetchingLocations.value = true
    const response = await BoqApi.getBoq(selectedInspectionId.value!!)
    boq.value = response.data

    if (boq.value.id) {
      const response = await BoqApi.getLocations(boq.value.id)
      locations.value = response.data
      fetchingLocations.value = false
      await requestWorks()
    } else {
      locations.value = []
      fetchingLocations.value = false
    }
    initialized.value = true
  }

  const buildAndRequestBoq = async () => {
    await BoqApi.buildBoq(selectedInspectionId.value!!)
    await requestBoq()
  }

  const updateLocation = async (location: BoqLocation) => {
    await BoqApi.updateLocation(location.id, toLocationUpdateRequest(location))
    await requestBoq()
  }

  const deleteLocation = async (id: number) => {
    await BoqApi.deleteLocation(id)
  }

  return {
    requestBoq,
    buildAndRequestBoq,
    updateLocation,
    deleteLocation,
  }
}
