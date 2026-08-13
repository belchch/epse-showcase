<template>
    <q-card bordered flat style="width: 400px;">
        <div class="text-caption q-ma-sm">{{ title }}</div>
        <div class="q-ma-md">
            <ReplacementToggles v-model:replacement="replacement" v-model:preservation="preservation" />

            <div class="q-pa-sm">
                <q-select label="Материал" v-model="material" :options="materials" option-label="name" option-value="id"
                    dense />
            </div>

            <div class="row q-gutter-md q-mt-xs q-pa-sm">
                <q-input type="number" v-model.number="length" :hint="lengthHint" dense style="width: 160px" />
                <div class="row items-center">
                    <q-btn outline size="xs" color="secondary" @click="emits('fillLength')">Равно
                        периметру</q-btn>
                </div>
            </div>
        </div>
    </q-card>
</template>
<script lang="ts" setup>
import { Material } from 'src/features/lookup/material/stores/types';
import ReplacementToggles from './ReplacementToggles.vue';
import { ref } from 'vue';
const replacement = defineModel<boolean>('replacement', { required: true })
const preservation = defineModel<boolean>('preservation', { required: true })
const length = defineModel<number>('length', { required: true })
const material = defineModel<Material | undefined>('material', { required: false })

const emits = defineEmits<{
    fillLength: []
}>()

defineProps<{
    title: string,
    lengthHint: string,
    materials: Material[]
}>()
</script>