<template>
    <q-card-section v-for="(section, index) in sections" :key="section.id">
        <q-separator />
        <div class="text-caption q-ma-sm text-accent">Секция {{ index + 1 }}</div>
        <div class="row q-pl-md q-gutter-md">
            <slot :section="section"></slot>
        </div>
    </q-card-section>
</template>
<script lang="ts" setup>
defineProps<{
    sections: {
        id: number,
        [key: string]: any
    }[]
}>()

const emits = defineEmits<{
    removeSection: [id: number]
    addSection: []
}>()
</script>