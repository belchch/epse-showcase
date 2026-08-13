import { defineStore } from "pinia";
import { ref } from "vue";
import { BoqLocation } from "../api/types";

export const useBoqLocationStore = defineStore('boq-location', () => {
    const floorPhotos = ref<string[]>()
    const ceilPhotos = ref<[]>()
    const doorPhotos = ref<[]>()
    const windowPhotos = ref<[]>()
    const wallPhotos = ref<[]>()
    const location = ref<BoqLocation>()

    return {
        floorPhotos,
        ceilPhotos,
        doorPhotos,
        windowPhotos,
        wallPhotos,
        location
    }
})