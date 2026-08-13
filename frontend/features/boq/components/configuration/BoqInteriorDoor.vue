<template>
    <div style="position: relative;">
        <div class="text-caption q-ma-sm text-accent">Дверь {{ index + 1 }}</div>
        <q-card-section>
            <div style="width: 180px">
                <q-select v-model="interiorDoorLocal.type" :options="doorTypeOptions" dense emit-value map-options
                    @update:model-value="updateInteriorDoor(true)" />
            </div>
            <div class="q-gutter-lg q-mt-md">
                <div>
                    <q-card bordered flat style="width: 500px;">
                        <div class="text-caption q-ma-sm">Размеры</div>
                        <q-card-section class="q-pt-none">
                            <div class="row q-gutter-xl">
                                <div>
                                    <LabeledValue label="Ширина" :value="interiorDoorLocal.width" />
                                    <LabeledValue label="Площадь" :value="interiorDoorLocal.area" :accent="true"/>
                                </div>
                                <div>
                                    <LabeledValue label="Высота" :value="interiorDoorLocal.height" />
                                    <LabeledValue label="Периметр" :value="interiorDoorLocal.perimeter" :accent="true"/>
                                </div>
                                <div>
                                    <LabeledValue label="Ширина наличника" :value="interiorDoor.slopesWidth" />
                                    <LabeledValue label="Площадь наличника" :value="interiorDoor.slopesArea" :accent="true" />                                
                                </div>
                            </div>                            
                        </q-card-section>
                    </q-card>
                </div>

                <div>
                    <div>
                        <ReplacementToggles v-model:replacement="interiorDoorLocal.replacement"
                            v-model:preservation="interiorDoorLocal.preservation" replacement-label="Замена двери"
                            @update:replacement="updateReplacement()"
                            @update:preservation="updateInteriorDoor(false)" />
                    </div>

                    <div class="q-mt-md">
                        <q-toggle color="secondary" v-model="interiorDoorLocal.trimsReplacement"
                            label="Замена наличников" size="sm" @update:model-value="updateInteriorDoor(false)" />

                        <q-toggle color="secondary" v-model="interiorDoorLocal.trimsPreservation" :disable="!interiorDoorLocal.trimsReplacement"
                            label="С сохранением" size="sm" @update:model-value="updateInteriorDoor(false)" />
                    </div>

                    <div class="q-mt-md">
                        <q-toggle color="secondary" v-model="interiorDoorLocal.hasSlopes" label="Наличие откосов"
                            size="sm" @update:model-value="updateInteriorDoor(false)" />
                    </div>
                </div>
            </div>
        </q-card-section>
    </div>
</template>
<script setup lang="ts">
import { computed, ref } from 'vue';
import { BoqInteriorDoorApi } from '../../api/interior-door/boq-interior-door-api';
import { BoqInteriorDoorModel, toInteriorDoorUpdateRequest } from '../../api/interior-door/types';
import { useBoqWorkService } from '../../composables/boq-work';
import ReplacementToggles from './common/ReplacementToggles.vue';
import LabeledValue from './common/LabeledValue.vue';

const { requestWorks } = useBoqWorkService()

const props = defineProps<{
    index: number,
    interiorDoor: BoqInteriorDoorModel
}>()

const emits = defineEmits<{
    remove: []
}>()

const doorTypeOptions = ref([
    {
        label: 'Межкомнатная',
        value: 'INTERIOR'
    },
    {
        label: 'Входная',
        value: 'ENTRANCE'
    },
])

// const selectedDoorType = computed(() => {
//     return doorTypeOptions.value.find(item => item.value == interiorDoorLocal.value.type)
// })

const interiorDoorLocal = ref<BoqInteriorDoorModel>(props.interiorDoor)

const updateReplacement = async () => {
    if (!interiorDoorLocal.value.replacement) {
        interiorDoorLocal.value.preservation = false
    }

    await updateInteriorDoor(false)
}

const updateTrimsReplacement = async () => {
    if (!interiorDoorLocal.value.trimsReplacement) {
        interiorDoorLocal.value.trimsPreservation = false
    }

    await updateInteriorDoor(false)
}

const updateInteriorDoor = async (updateVolume: boolean) => {
    const response = await BoqInteriorDoorApi.updateInteriorDoor(
        interiorDoorLocal.value.id,
        toInteriorDoorUpdateRequest(interiorDoorLocal.value),
        updateVolume
    )

    interiorDoorLocal.value = response.data
    await requestWorks()
}

</script>
<style scoped>
.computed-value {
    overflow: hidden;
    text-overflow: ellipsis;
    width: 60px;
}
</style>