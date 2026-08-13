<template>
    <div>
        {{ value }}
    </div>
    <div v-if="valueChanged" style="font-size: 9px; position: absolute; top: 4px; left: 4px;" class="text-accent">
        {{ originalValue }}
    </div>
    <q-popup-edit v-if="canEdit" v-model="localValue" buttons v-slot="scope" @update:model-value="updateValue">
        <q-input bottom-slots type="number" v-model="scope.value" dense autofocus > 
            <template v-slot:append>
                <q-icon name="settings_backup_restore" @click="scope.value = originalValue"/>
            </template>
        </q-input>
    </q-popup-edit>
</template>
<script setup lang="ts">
import { computed, ref } from 'vue';
import _ from 'lodash';
import { useBoqService } from '../composables/boq';
import { BoqWork, toWorkUpdateRequest } from '../api/types';
import { BoqWorkApi } from '../api/boq-work-api';
import { useBoqWorkStore } from '../stores/boq-work-store';
import { useBoqWorkService } from '../composables/boq-work';
import { useEstimateService } from 'src/features/estimate/composables/estimate-service'

const { updateWork } = useBoqWorkService()
const { getEstimate } = useEstimateService()

const { field, value, originalValue, row, canEdit = true } = defineProps<{
    field: string,
    value: number,
    originalValue: number,
    row: BoqWork,
    canEdit?: boolean
}>()

const localValue = ref(value)
const valueChanged = computed(() => originalValue != value)

const resetValue = () => {
    localValue.value = originalValue    
}

const updateValue = async () => {
    const update = _.cloneDeep(row) as any
    update[field] = localValue.value
    await updateWork(update, true)
    await getEstimate()
}
</script>
