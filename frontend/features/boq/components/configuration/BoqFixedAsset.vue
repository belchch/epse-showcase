<template>
    <div style="position: relative;">
        <div class="text-caption q-ma-sm text-accent">{{ `${prefix} ${index + 1}` }}</div>

        <q-card-section>
            <q-card bordered flat style="width: 400px;">
                <div class="text-caption q-ma-sm">Размеры</div>
                <q-card-section class="q-pt-none">
                    <div class="row q-gutter-x-xl">
                        <div>                        
                            <LabeledValue label="Ширина" :value="fixedAssetLocal.height" />
                            <LabeledValue label="Площадь" :value="fixedAssetLocal.area" />                            
                        </div>
                        <div>                            
                            <LabeledValue label="Высота" :value="fixedAssetLocal.height" />
                            <LabeledValue label="Периметр" :value="fixedAssetLocal.perimeter" />                            
                        </div>
                        <div>             
                            <LabeledValue label="Длина" :value="fixedAssetLocal.length" />                        
                        </div>
                    </div>                    
                    <div class="row q-gutter-md q-mt-none">
                        <LabeledValue label="Полщадь поверхности" :value="fixedAssetLocal.surfaceArea" :accent="true" />
                    </div>
                </q-card-section>
            </q-card>

            <div class="q-mt-md">
                <q-toggle color="secondary" v-model="fixedAssetLocal.plaster" label="Штукатурка" size="sm"
                    @update:model-value="updateFixedAsset(false)" />
            </div>
            <div class="row q-gutter-x-lg q-mt-md">
                <MaterialReplacement v-model:material="fixedAssetLocal.material"
                    v-model:replacement="fixedAssetLocal.replacement" v-model:preservation="fixedAssetLocal.painting"
                    :materials="materials" @update:material="updateFixedAsset(false)"
                    @update:preservation="updateFixedAsset(false)" @update:replacement="updateReplacement()"
                    preservation-label="С окраской" />
            </div>
        </q-card-section>
    </div>
</template>
<script setup lang="ts">
import { computed, ref } from 'vue';
import { useBoqWorkService } from '../../composables/boq-work';
import MaterialReplacement from './common/MaterialReplacement.vue';
import { BoqFixedAssetModel, toFixedAssetUpdateRequest } from '../../api/fixed-asset/types';
import { BoqFixedAssetApi } from '../../api/fixed-asset/boq-fixed-asset-api';
import LabeledValue from './common/LabeledValue.vue';

const { requestWorks } = useBoqWorkService()

const props = defineProps<{
    index: number,
    prefix: string,
    fixedAsset: BoqFixedAssetModel
}>()

const materials = computed(() => {
    return fixedAssetLocal.value.structElems.flatMap(item => item.materials)
})

const emits = defineEmits<{
    remove: []
}>()

const fixedAssetLocal = ref<BoqFixedAssetModel>(props.fixedAsset)

const updateReplacement = async () => {
    if (!fixedAssetLocal.value.replacement) {
        fixedAssetLocal.value.painting = false
    }

    await updateFixedAsset(false)
}

const updateFixedAsset = async (updateVolume: boolean) => {
    const response = await BoqFixedAssetApi.updateFixedAsset(
        fixedAssetLocal.value.id,
        toFixedAssetUpdateRequest(fixedAssetLocal.value),
        updateVolume
    )

    fixedAssetLocal.value = response.data
    await requestWorks()
}

</script>