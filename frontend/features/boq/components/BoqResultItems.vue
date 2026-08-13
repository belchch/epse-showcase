<template>
  <q-card>
    <q-tabs
      v-model="tab"
      no-caps
      dense
      active-color="primary"
      indicator-color="primary"
      align="justify"
      narrow-indicator
    >
      <q-tab name="works" label="Работы" />
      <q-tab name="materials" label="Материалы" />
    </q-tabs>
    <q-separator />

    <q-tab-panels v-model="tab">
      <q-tab-panel name="works" class="q-pa-xs">
        <WorkTable
          :works="works"
          :fetchingWorks="fetchingWorks"
          :editable="true"
          :show-location="true"
          :flat="true"
        >
          <template v-slot:additional-top>
            <CommonWorksEditor />
          </template>
        </WorkTable>
      </q-tab-panel>
      <q-tab-panel name="materials" class="q-pa-xs">
        <MaterialTable />
      </q-tab-panel>
    </q-tab-panels>
  </q-card>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import { useBoqWorkStore } from '../stores/boq-work-store'
import { storeToRefs } from 'pinia'
import MaterialTable from './MaterialTable.vue'
import CommonWorksEditor from './CommonWorksEditor.vue'
import WorkTable from './WorkTable.vue'

const { works, fetchingWorks } = storeToRefs(useBoqWorkStore())
type TabVal = 'works' | 'materials'
const tab = ref<TabVal>('works')
</script>
