<template>
    <div class="row">
        <div class="text-caption text-grey-8">{{ label }}:</div>
        <div class="q-ml-sm computed-value" :class="accent ? 'text-accent' : ''">
            {{ value != undefined ? value : '-' }}
            <q-tooltip>{{ value }}</q-tooltip>
        </div>
    </div>
</template>
<script lang="ts" setup>
const props = withDefaults(defineProps<{
    value?: number | undefined,
    label: string,
    accent?: boolean
}>(), {
    accent: false
})
</script>