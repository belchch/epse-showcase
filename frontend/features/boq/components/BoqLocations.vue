<template>
  <q-table
    :rows="locations || []"
    :columns="columns"
    :row-key="(row) => row.id"
    wrap-cells
    :pagination="{ rowsPerPage: 20 }"
    separator="cell"
    :loading="fetchingLocations"
  >
    <template v-slot:loading>
      <q-inner-loading showing color="primary" />
    </template>
    <template v-slot:top>
      <div class="row q-gutter-md full-width justify-between">
        <template v-if="boq">
          <DownloadReportButton
            label="Скачать"
            :disable="false"
            :api-fn="async () => (await BoqApi.buildReport(selectedInspectionId!!)).data"
          />
          <q-btn
            v-if="$q.screen.lt.md"
            outline
            icon-right="menu"
            label="Работы"
            @click="openDrawer"
          />
        </template>
      </div>
    </template>
    <template v-slot:body="props">
      <q-tr :props="props">
        <q-td key="name" :props="props" @click="navigateLocation(props.row)">
          <div class="text-accent" style="cursor: pointer">
            {{ props.row.room.name }} {{ props.row.roomNum }}
          </div>
        </q-td>
        <q-td key="area" :props="props" class="cell-edit">
          {{ props.row.area }}
        </q-td>
        <q-td key="height" class="cell-edit">
          {{ props.row.height }}
        </q-td>
        <q-td key="perimeter" :props="props" class="cell-edit">
          {{ props.row.perimeter }}
        </q-td>
      </q-tr>
    </template>
  </q-table>
</template>
<script lang="ts" setup>
import { useBoqStore } from '../stores/boq-store'
import { storeToRefs } from 'pinia'
import { BoqLocation } from '../api/types'
import { BoqApi } from '../api/boq-api'
import DownloadReportButton from 'src/components/DownloadReportButton.vue'
import { useInspectionsStore } from 'src/features/inspection/store/inspection-store'

const emits = defineEmits<{
  navigateLocation: [location: BoqLocation]
  openDrawer: []
}>()
const { locations, boq, initialized, fetchingLocations } = storeToRefs(useBoqStore())
const { selectedInspectionId } = storeToRefs(useInspectionsStore())

const navigateLocation = (location: BoqLocation) => {
  emits('navigateLocation', location)
}
const openDrawer = () => {
  emits('openDrawer')
}

const columns = [
  {
    name: 'name',
    field: 'name',
    label: 'Локация',
    align: 'left' as const,
  },
  {
    name: 'area',
    field: 'area',
    label: 'Общая площадь',
    align: 'left' as const,
  },
  {
    name: 'height',
    field: 'height',
    label: 'Высота потолков',
    align: 'left' as const,
  },
  {
    name: 'perimeter',
    field: 'perimeter',
    label: 'Периметр',
    align: 'left' as const,
  },
]
</script>
