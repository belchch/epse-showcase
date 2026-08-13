<template>
    <div>
        <div class="row justify-between">
            <q-card-section>
                <BaseboardReplacement title="Напольный плинтус" length-hint="Длина напольного плинтуса"
                    v-model:replacement="floorLocal.baseboardReplacement"
                    v-model:preservation="floorLocal.baseboardPreservation" v-model:length="floorLocal.baseboardLength"
                    v-model:material="floorLocal.baseboardMaterial" :materials="floorMaterials"                    
                    @update:replacement="updateBaseboardReplacement" @update:material="updateFloor(floorLocal, false)"
                    @update:preservation="updateFloor(floorLocal, false)" @update:length="updateFloor(floorLocal, true)"
                    @fill-length="baseboardLengthAsPerimeter" />
            </q-card-section>
            <div>
                <PhotoGallery :urls="floorPhotos" />
            </div>
        </div>
        <SectionLayout :sections="floorLocal.sections" @add-section="addSection" @remove-section="deleteSection">
            <template v-slot="{ section }">
                <div class="q-gutter-md" style="width: 400px;">
                    <LabeledValue label="Площадь" :value="section.area" :accent="true" />

                    <div>
                        <q-toggle color="secondary" v-model="section.screedLeveling" label="Выравнивание стяжки"
                            @update:model-value="updateFloorSection(section as BoqFloorSectionModel, false)" />

                        <q-toggle color="secondary" v-model="section.antisepticTreatment"
                            label="Антисептическая обработка"
                            @update:model-value="updateFloorSection(section as BoqFloorSectionModel, false)" />
                    </div>
                </div>
                <div>
                    <MaterialReplacement v-model:replacement="section.materialReplacement"
                        v-model:preservation="section.materialPreservation" v-model:material="section.material"
                        :materials="floorSectionMaterials"
                        @update:replacement="(val: boolean) => updateFloorSectionMaterialReplacement(section as BoqFloorSectionModel, val)"
                        @update:preservation="updateFloorSection(section as BoqFloorSectionModel, false)"
                        @update:material="updateFloorSection(section as BoqFloorSectionModel, false)" />
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
import { computed, onUnmounted, ref } from 'vue';
import { BoqFloorApi } from '../../api/floor/boq-floor-api';
import { BoqFloorModel, BoqFloorSectionModel, toFloorSectionUpdateRequest, toFloorUpdateRequest } from '../../api/floor/types';
import { useBoqWorkService } from '../../composables/boq-work';
import { useBoqLocationStore } from '../../stores/boq-location-store';
import PhotoGallery from './common/PhotoGallery.vue';
import SectionLayout from './common/SectionLayout.vue';
import LabeledValue from './common/LabeledValue.vue';
import { BoqSection } from 'src/features/lookup/struct-elem/stores/types';

const { floorPhotos, location } = storeToRefs(useBoqLocationStore())
const { requestWorks } = useBoqWorkService()

const props = defineProps<{
    floor: BoqFloorModel
}>()

const floorLocal = ref<BoqFloorModel>(props.floor)

const addSection = async () => {
    const response = await BoqFloorApi.createFloorSection(props.floor.id!)
    floorLocal.value.sections.push(response.data)
}

const deleteSection = async (id: number) => {
    await BoqFloorApi.deleteFloorSection(id)
    await requestWorks()
    floorLocal.value.sections = floorLocal.value.sections.filter(item => item.id != id)
}

const updateBaseboardReplacement = async () => {
    if (!floorLocal.value.baseboardReplacement) {
        floorLocal.value.baseboardPreservation = false
    }

    await updateFloor(floorLocal.value, false)
}

const updateFloorSectionMaterialReplacement = async (section: BoqFloorSectionModel, value: boolean) => {
    if (!value) {
        section.materialPreservation = false
    }

    await updateFloorSection(section, false)
}

const baseboardLengthAsPerimeter = async () => {
    floorLocal.value.baseboardLength = location.value?.perimeter || 0
    await updateFloor(floorLocal.value, true)
}

const updateFloorSection = async (floorSection: BoqFloorSectionModel, updateVolume: boolean) => {
    await BoqFloorApi.updateFloorSection(floorSection.id, toFloorSectionUpdateRequest(floorSection), updateVolume)
    await requestWorks()
}

const updateFloor = async (floor: BoqFloorModel, updateVolume: boolean) => {
    await BoqFloorApi.updateFloor(floor.id, toFloorUpdateRequest(floor), updateVolume)
    await requestWorks()
}

const materials = (boqSection: BoqSection) => {
    return props.floor.structElems.filter(item => item.boqSection == boqSection).flatMap(item => item.materials)
}

const floorMaterials = ref<Material[]>(materials('FLOOR'))
const floorSectionMaterials = ref<Material[]>(materials('FLOOR_SECTION'))

</script>
<style scoped>
.photo {
    height: 200px;
    width: 160px;
}
</style>