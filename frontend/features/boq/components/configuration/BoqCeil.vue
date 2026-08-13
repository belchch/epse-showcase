<template>
    <div>
        <div class="row justify-between">
            <q-card-section>
                <BaseboardReplacement title="Потолочный плинтус" length-hint="Длина потолочного плинтуса"
                    v-model:replacement="ceilLocal.moldingReplacement"
                    v-model:preservation="ceilLocal.moldingPreservation" v-model:length="ceilLocal.moldingLength"
                    v-model:material="ceilLocal.moldingMaterial" :materials="ceilMaterials"
                    @update:replacement="updateBaseboardReplacement" @update:preservation="updateCeil(ceilLocal, false)"
                    @update:material="updateCeil(ceilLocal, false)" @update:length="updateCeil(ceilLocal, true)"
                    @fill-length="moldingLengthAsPerimeter" />
            </q-card-section>
            <div>
                <PhotoGallery :urls="ceilPhotos" />
            </div>
        </div>
        <SectionLayout :sections="ceilLocal.sections" @add-section="addSection" @remove-section="deleteSection">
            <template v-slot="{ section }">
                <div class="q-gutter-md" style="width: 400px;">
                    <LabeledValue label="Площадь" :value="section.area" :accent="true" />


                    <q-toggle color="secondary" v-model="section.antisepticTreatment" label="Антисептическая обработка"
                        @update:model-value="updateCeilSection(section as BoqCeilSectionModel, false)" />
                </div>
                <div>
                    <MaterialReplacement v-model:replacement="section.materialReplacement"
                        v-model:preservation="section.materialPreservation" v-model:material="section.material"
                        :materials="ceilSectionMaterials"
                        @update:replacement="(val: boolean) => updateCeilSectionMaterialReplacement(section as BoqCeilSectionModel, val)"
                        @update:preservation="updateCeilSection(section as BoqCeilSectionModel, false)"
                        @update:material="updateCeilSection(section as BoqCeilSectionModel, false)" />
                </div>
            </template>
        </SectionLayout>
    </div>
</template>
<script lang="ts" setup>
import MaterialReplacement from './common/MaterialReplacement.vue';
import BaseboardReplacement from './common/BaseboardReplacement.vue';
import { storeToRefs } from 'pinia';
import { Material } from 'src/features/lookup/material/stores/types';
import { ref } from 'vue';
import { BoqCeilApi } from '../../api/ceil/boq-ceil-api';
import { BoqCeilModel, BoqCeilSectionModel, toCeilSectionUpdateRequest, toCeilUpdateRequest } from '../../api/ceil/types';
import { useBoqWorkService } from '../../composables/boq-work';
import { useBoqLocationStore } from '../../stores/boq-location-store';
import PhotoGallery from './common/PhotoGallery.vue';
import SectionLayout from './common/SectionLayout.vue';
import LabeledValue from './common/LabeledValue.vue';
import { BoqSection } from 'src/features/lookup/struct-elem/stores/types';

const { ceilPhotos, location } = storeToRefs(useBoqLocationStore())
const { requestWorks } = useBoqWorkService()

const props = defineProps<{
    ceil: BoqCeilModel
}>()

const ceilLocal = ref<BoqCeilModel>(props.ceil)

const addSection = async () => {
    const response = await BoqCeilApi.createCeilSection(props.ceil.id!)
    ceilLocal.value.sections.push(response.data)
}

const deleteSection = async (id: number) => {
    await BoqCeilApi.deleteCeilSection(id)
    ceilLocal.value.sections = ceilLocal.value.sections.filter(item => item.id != id)
}

const updateBaseboardReplacement = async () => {
    if (!ceilLocal.value.moldingReplacement) {
        ceilLocal.value.moldingPreservation = false
    }

    await updateCeil(ceilLocal.value, false)
}

const updateCeilSectionMaterialReplacement = async (section: BoqCeilSectionModel, value: boolean) => {
    if (!value) {
        section.materialPreservation = false
    }

    await updateCeilSection(section, false)
}

const moldingLengthAsPerimeter = async () => {
    ceilLocal.value.moldingLength = location.value?.perimeter || 0
    await updateCeil(ceilLocal.value, true)
}

const updateCeilSection = async (ceilSection: BoqCeilSectionModel, updateVolume: boolean) => {
    await BoqCeilApi.updateCeilSection(ceilSection.id, toCeilSectionUpdateRequest(ceilSection), updateVolume)
    await requestWorks()
}

const updateCeil = async (ceil: BoqCeilModel, updateVolume: boolean) => {
    await BoqCeilApi.updateCeil(ceil.id, toCeilUpdateRequest(ceil), updateVolume)
    await requestWorks()
}

const materials = (boqSection: BoqSection) => {
    return props.ceil.structElems.filter(item => item.boqSection == boqSection).flatMap(item => item.materials)
}

const ceilMaterials = ref<Material[]>(materials('CEIL'))
const ceilSectionMaterials = ref<Material[]>(materials('CEIL_SECTION'))
</script>
<style scoped>
.photo {
    height: 200px;
    width: 160px;
}
</style>