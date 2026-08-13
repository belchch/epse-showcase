<template>
    <div class="row justify-between">
        <PhotoGallery :urls="doorPhotos"/>
    </div>
    <q-separator class="q-mt-md"/>
    <template v-for="(interiorDoor, index) in interiorDoorsLocal" :key="interiorDoor.id">
        <BoqInteriorDoor :interior-door="interiorDoor" :index="index" @remove="deleteInteriorDoor(interiorDoor.id)"/>
    </template>
</template>

<script setup lang="ts">
import { storeToRefs } from 'pinia';
import { BoqInteriorDoorApi } from '../../api/interior-door/boq-interior-door-api';
import { BoqInteriorDoorModel } from '../../api/interior-door/types';
import { useBoqLocationStore } from '../../stores/boq-location-store';
import BoqInteriorDoor from './BoqInteriorDoor.vue';
import { ref } from 'vue';
import { useBoqWorkService } from '../../composables/boq-work';
import PhotoGallery from './common/PhotoGallery.vue';

const { location, doorPhotos } = storeToRefs(useBoqLocationStore())
const { requestWorks } = useBoqWorkService()

const props = defineProps<{
    interiorDoors: BoqInteriorDoorModel[]
}>()

const interiorDoorsLocal = ref<BoqInteriorDoorModel[]>(props.interiorDoors)

const addInteriorDoor = async () => {
    const response = await BoqInteriorDoorApi.createInteriorDoor(location.value!.id)
    interiorDoorsLocal.value.push(response.data)
}

const deleteInteriorDoor = async (id: number) => {
    await BoqInteriorDoorApi.deleteInteriorDoor(id)
    await requestWorks()
    interiorDoorsLocal.value = interiorDoorsLocal.value.filter(item => item.id != id)    
}
</script>
