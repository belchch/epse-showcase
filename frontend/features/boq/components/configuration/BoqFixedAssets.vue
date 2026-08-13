<template>
    <div class="row justify-between">
        <PhotoGallery :urls="wallPhotos"/>
    </div>
    <q-separator class="q-mt-md"/>
    <template v-for="(fixedAsset, index) in fixedAssetsLocal" :key="fixedAsset.id">
        <BoqFixedAsset :fixed-asset="fixedAsset" :index="index" @remove="deleteFixedAsset(fixedAsset.id)" :prefix="prefix"/>
    </template>
</template>

<script setup lang="ts">
import { storeToRefs } from 'pinia';
import { useBoqLocationStore } from '../../stores/boq-location-store';
import BoqFixedAsset from './BoqFixedAsset.vue';
import PhotoGallery from './common/PhotoGallery.vue';
import { ref } from 'vue';
import { useBoqWorkService } from '../../composables/boq-work';
import { BoqFixedAssetModel } from '../../api/fixed-asset/types';
import { BoqFixedAssetApi } from '../../api/fixed-asset/boq-fixed-asset-api';

const { location, wallPhotos } = storeToRefs(useBoqLocationStore())
const { requestWorks } = useBoqWorkService()

const props = defineProps<{
    prefix: string,
    fixedAssets: BoqFixedAssetModel[]
}>()

const fixedAssetsLocal = ref<BoqFixedAssetModel[]>(props.fixedAssets)

const addFixedAsset = async () => {
    const response = await BoqFixedAssetApi.createFixedAsset(location.value!.id)
    fixedAssetsLocal.value.push(response.data)
}

const deleteFixedAsset = async (id: number) => {
    await BoqFixedAssetApi.deleteFixedAsset(id)
    await requestWorks()
    fixedAssetsLocal.value = fixedAssetsLocal.value.filter(item => item.id != id)    
}
</script>