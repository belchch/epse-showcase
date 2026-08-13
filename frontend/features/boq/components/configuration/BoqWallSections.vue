<template>
    <div class="row justify-between">
        <PhotoGallery :urls="wallPhotos"/>
    </div>
    <q-separator class="q-mt-md"/>
    <template v-for="(wallSection, index) in wallSectionsLocal" :key="wallSection.id">
        <BoqWallSection :wall-section="wallSection" :index="index" @remove="deleteWallSection(wallSection.id)"/>
    </template>
</template>

<script setup lang="ts">
import { storeToRefs } from 'pinia';
import { BoqWallSectionApi } from '../../api/wall/boq-wall-section-api';
import { BoqWallSectionModel } from '../../api/wall/types';
import { useBoqLocationStore } from '../../stores/boq-location-store';
import BoqWallSection from './BoqWallSection.vue';
import PhotoGallery from './common/PhotoGallery.vue';
import { ref } from 'vue';
import { useBoqWorkService } from '../../composables/boq-work';

const { location, wallPhotos } = storeToRefs(useBoqLocationStore())
const { requestWorks } = useBoqWorkService()

const props = defineProps<{
    wallSections: BoqWallSectionModel[]
}>()

const wallSectionsLocal = ref<BoqWallSectionModel[]>(props.wallSections)

const addWallSection = async () => {
    const response = await BoqWallSectionApi.createWallSection(location.value!.id)
    wallSectionsLocal.value.push(response.data)
}

const deleteWallSection = async (id: number) => {
    await BoqWallSectionApi.deleteWallSection(id)
    await requestWorks()
    wallSectionsLocal.value = wallSectionsLocal.value.filter(item => item.id != id)    
}
</script>
