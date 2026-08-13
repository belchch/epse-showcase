<template>
    <div class="row justify-between">
        <PhotoGallery :urls="windowPhotos"/>
    </div>
    <q-separator class="q-mt-md"/>
    <template v-for="(window, index) in windowsLocal" :key="window.id">
        <BoqWindow :window="window" :index="index" @remove="deleteWindow(window.id)"/>
    </template>
</template>

<script setup lang="ts">
import { storeToRefs } from 'pinia';
import { BoqWindowApi } from '../../api/window/boq-window-api';
import { BoqWindowModel } from '../../api/window/types';
import { useBoqLocationStore } from '../../stores/boq-location-store';
import BoqWindow from './BoqWindow.vue';
import PhotoGallery from './common/PhotoGallery.vue';
import { ref } from 'vue';
import { useBoqWorkService } from '../../composables/boq-work';

const { location, windowPhotos } = storeToRefs(useBoqLocationStore())
const { requestWorks } = useBoqWorkService()

const props = defineProps<{
    windows: BoqWindowModel[]
}>()

const windowsLocal = ref<BoqWindowModel[]>(props.windows)

const addWindow = async () => {
    const response = await BoqWindowApi.createWindow(location.value!.id)
    windowsLocal.value.push(response.data)
}

const deleteWindow = async (id: number) => {
    await BoqWindowApi.deleteWindow(id)
    await requestWorks()
    windowsLocal.value = windowsLocal.value.filter(item => item.id != id)    
}
</script>
