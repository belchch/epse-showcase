<template>
    <div class="row q-gutter-md">
        <q-toggle color="secondary" v-model="replacement" :label="replacementLabel" size="sm"/>

        <template v-if="showPreservation">
          <q-toggle color="secondary" v-model="preservation" :label="preservationLabel" size="sm" :disable="!replacement" />
        </template>
    </div>
</template>
<script lang="ts" setup>
const replacement = defineModel<boolean>('replacement', { required: true })
const preservation = defineModel<boolean>('preservation', { required: true })

const props =  withDefaults(defineProps<{
  replacementLabel?: string | undefined,
  preservationLabel?: string | undefined,
  showPreservation?: boolean | undefined
}>(), {
  replacementLabel: 'Замена',
  preservationLabel: 'С сохранением',
  showPreservation: true
})
</script>