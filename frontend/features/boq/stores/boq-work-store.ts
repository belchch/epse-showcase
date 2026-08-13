import { defineStore } from 'pinia'
import { BoqWork } from '../api/types'
import { ref } from 'vue'

export const useBoqWorkStore = defineStore('boq-work', () => {
  const works = ref<BoqWork[]>([])
  const fetchingWorks = ref(false)

  return {
    works,
    fetchingWorks,
  }
})
