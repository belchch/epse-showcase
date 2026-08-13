<template>
    {{ value }}
    <q-popup-edit v-if="canEdit" v-model="localValue" buttons v-slot="scope" @update:model-value="updateValue">
        <q-input type="number" v-model="scope.value" dense autofocus />
    </q-popup-edit>
    <q-icon name="edit" class="edit-icon" />
</template>
<script setup lang="ts">
import { ref } from 'vue';
import _ from 'lodash';
import { BoqLocation } from '../api/types';
import { useBoqService } from '../composables/boq';

const { updateLocation } = useBoqService()

const { field, value, row, canEdit = true } = defineProps<{
    field: string,
    value: number,
    row: BoqLocation,
    canEdit?: boolean
}>()

const localValue = ref(value)

const updateValue = async () => {
    const update = _.cloneDeep(row) as any
    update[field] = localValue.value
    await updateLocation(update as BoqLocation)
}
</script>
