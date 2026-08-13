<template>
    <div>
        <q-card bordered flat style="width: 400px;">
            <div class="text-caption q-ma-sm">{{ title || 'Материал' }}</div>
            <div class="q-ma-md">
                <ReplacementToggles v-model:replacement="replacement" v-model:preservation="preservation"
                    :preservation-label="preservationLabel" :replacement-label="replacementLabel" :show-preservation="showPreservation"/>
                <q-select v-model="material" :options="materials" option-label="name" option-value="id" dense />
            </div>
        </q-card>
    </div>
</template>
<script lang="ts" setup>
import { Material } from 'src/features/lookup/material/stores/types';
import { ref } from 'vue';
import ReplacementToggles from './ReplacementToggles.vue';

const replacement = defineModel<boolean>('replacement', { required: true })
const preservation = defineModel<boolean>('preservation', { required: true })
const material = defineModel<Material>('material')

withDefaults(defineProps<{
    materials: Material[],
    preservationLabel?: string,
    replacementLabel?: string,
    title?: string,
    showPreservation?: boolean
}>(), {
    showPreservation: true
})
</script>