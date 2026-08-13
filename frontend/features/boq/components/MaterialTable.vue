<template>
  <q-table
    :rows="materials"
    :columns="columns"
    :row-key="(row) => row.id"
    wrap-cells
    flat
    :bordered="false"
    selection="multiple"
    :pagination="{ rowsPerPage: 20 }"
    separator="cell"
    :loading="fetchingMaterials"
  >
    <template v-slot:loading>
      <q-inner-loading showing color="primary" />
    </template>
    <template v-slot:body="props">
      <q-tr :props="props">
        <q-td>
          <q-toggle
            :model-value="!props.row.disabled"
            @update:model-value="(val: boolean) => setMaterialDisabled(props.row, !val)"
            size="xs"
            color="secondary"
          />
        </q-td>
        <q-td key="name" :props="props">
          {{ props.row.rawMaterial.name }}
          <div class="text-caption text-grey-8">{{ props.row.work?.rate?.name }}</div>
        </q-td>
        <q-td key="unitOfMeasure" :props="props">
          {{ uomDescription(props.row.rawMaterial.unitOfMeasure) }}
        </q-td>
        <q-td key="volume" class="ceil-edit">
          <MaterialCellEditor field="volume" :row="props.row" :value="props.row.volume" :original-value="props.row.calculatedVolume"/>
          <q-icon name="edit" class="edit-icon" />
        </q-td>
        <q-td key="location" :props="props">
          {{ props.row.work && locationName(props.row.work) }}
        </q-td>
      </q-tr>
    </template>

    <template v-slot:header-selection>
      <q-toggle
        :model-value="disableAllValue()"
        @update:model-value="(val: boolean) => setDisabledAll(!val)"
        size="xs"
        color="secondary"
      />
    </template>
  </q-table>
</template>
<script lang="ts" setup>
import { uomDescription } from 'src/features/rate/stores/types'
import { BoqRawMaterial, BoqWork } from '../api/types'
import MaterialCellEditor from './MaterialCellEditor.vue'
import { ref } from 'vue'
import { useBoqRawMaterialService } from '../composables/boq-material'
import { useMaterialStore } from 'src/features/lookup/material/stores/material-store'
import { storeToRefs } from 'pinia'
import { useBoqMaterialStore } from '../stores/boq-material-store'
const { updateMaterial, requestMaterials } = useBoqRawMaterialService()
const { materials, fetchingMaterials } = storeToRefs(useBoqMaterialStore())

const setDisabledAll = (value: boolean) => {
  materials.value.forEach(async (item) => {
    item.disabled = value
    await updateMaterial(item)
  })
}

const disableAllValue = () => {
  return materials.value.filter((item) => item.disabled).length == 0
}

const setMaterialDisabled = async (material: BoqRawMaterial, disabled: boolean) => {
  material.disabled = disabled
  await updateMaterial(material)
}

const locationName = (row: BoqWork) => `${row.roomName || ''} ${row.roomNum || ''}`

const columns = [
  {
    name: 'name',
    field: (row: BoqRawMaterial) => row.rawMaterial.name,
    label: 'Материал',
    align: 'left' as const,
    sortable: true,
  },
  {
    name: 'unitOfMeasure',
    field: (row: BoqRawMaterial) => uomDescription(row.rawMaterial.unitOfMeasure),
    label: 'Ед. изм.',
    align: 'left' as const,
    sortable: true,
  },
  {
    name: 'volume',
    field: (row: BoqRawMaterial) => row.volume,
    label: 'Количество',
    align: 'left' as const,
    sortable: true,
  },
  {
    name: 'location',
    field: 'location',
    label: 'Локация',
    align: 'left' as const,
    sortable: true,
  },
]
</script>
<style scoped>
.ceil-edit .edit-icon {
  opacity: var(--e-opacity-edit-icon);
  position: absolute;
  top: 2px;
  right: 2px;
}

.ceil-edit:hover {
  cursor: pointer;
}
</style>
