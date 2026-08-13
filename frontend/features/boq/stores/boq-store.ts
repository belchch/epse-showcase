import { defineStore } from 'pinia'
import { ref } from 'vue'
import { Boq, BoqLocation } from '../api/types'

export const useBoqStore = defineStore('boq', () => {
  const boq = ref<Boq>()
  const locations = ref<BoqLocation[]>()
  const fetchingLocations = ref(false)
  const initialized = ref(false)

  return {
    boq,
    locations,
    initialized,
    fetchingLocations,
  }
})
