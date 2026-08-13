import { defineStore } from 'pinia'
import { BoqRawMaterial } from '../api/types'
import { ref } from 'vue'

export const useBoqMaterialStore = defineStore('boq-material', () => {
  const materials = ref<BoqRawMaterial[]>([])
  const fetchingMaterials = ref(false)

  return {
    materials,
    fetchingMaterials,
  }
})
