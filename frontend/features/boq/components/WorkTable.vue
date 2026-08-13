<template>
  <q-table :rows="filteredWorks" :columns="columns" :row-key="(row) => row.id" wrap-cells :bordered="false"
    :selection="editable ? 'multiple' : 'none'" :pagination="{ rowsPerPage: 20 }" separator="cell"
    :loading="fetchingWorks">
    <template v-slot:loading>
      <q-inner-loading showing color="primary" />
    </template>
    <template v-slot:top>
      <div class="row justify-between" style="width: 100%;">        
        <slot name="additional-top"></slot>
        <q-input v-model="search" outlined type="search" dense class="col q-ml-md">
          <template v-slot:append>
            <q-icon name="search" />
          </template>
        </q-input>
      </div>
    </template>
    <template v-slot:body="props">
      <q-tr :props="props">
        <q-td v-if="editable">
          <q-toggle :model-value="!props.row.disabled"
            @update:model-value="(val: boolean) => setWorkDisabled(props.row, !val)" size="xs" color="secondary" />
        </q-td>
        <q-td key="name" :props="props">
          {{ props.row.rate.name }}
          <div class="text-caption text-grey-8">
            {{
              props.row.rawMaterials
                ?.map((material: BoqRawMaterial) => material?.rawMaterial?.name)
                .join(', ') || 'Без материалов'
            }}
          </div>
        </q-td>
        <q-td key="unitOfMeasure" :props="props">
          {{ uomDescription(props.row.rate.unitOfMeasure) }}
        </q-td>
        <q-td key="volume" :class="editable ? 'ceil-edit' : ''">
          <template v-if="editable">
            <WorkCellEditor field="volume" :row="props.row" :value="props.row.volume" :original-value="props.row.calculatedVolume"/>
            <q-icon name="edit" class="edit-icon" />
          </template>
          <div v-else>{{ props.row.volume }}</div>
        </q-td>
        <q-td v-if="showLocation" key="location" :props="props">
          {{ locationName(props.row) }}
        </q-td>
      </q-tr>
    </template>

    <template v-slot:header-selection>
      <q-toggle :model-value="disableAllValue()" @update:model-value="(val: boolean) => setDisabledAll(!val)" size="xs"
        color="secondary" />
    </template>
  </q-table>
</template>
<script lang="ts" setup>
import { uomDescription } from 'src/features/rate/stores/types'
import { BoqWork, BoqRawMaterial } from '../api/types'
import WorkCellEditor from './WorkCellEditor.vue'
import { computed, ref } from 'vue'
import { useBoqWorkService } from '../composables/boq-work'
import { useEstimateService } from 'src/features/estimate/composables/estimate-service'
import { ParamsType } from 'src/features/lookup/rate/types'
const { updateWork } = useBoqWorkService()
const estimateService = useEstimateService()
const props = defineProps<{
  works: BoqWork[]
  editable: boolean
  showLocation: boolean
  fetchingWorks: boolean
  workParamsType?: ParamsType[]
}>()

const search = ref<string>()

const filteredWorks = computed(() => {
  let result = props.works

  if (props.workParamsType) {
    result = result.filter(item => props.workParamsType?.includes(item.rate.boqWorkParamsType))
  }

  if (search.value) {
    result = result.filter(item => item.rate.name.toLowerCase().includes(search.value!.toLocaleLowerCase()))
  }

  return result
})

const setDisabledAll = (value: boolean) => {
  props.works.forEach(async (item) => {
    item.disabled = value
    await updateWork(item, false)
  })
}

const disableAllValue = () => {
  return props.works.filter((item) => item.disabled).length == 0
}

const setWorkDisabled = async (work: BoqWork, disabled: boolean) => {
  work.disabled = disabled
  await updateWork(work, false)
  await estimateService.getEstimate()
}

const locationName = (row: BoqWork) => `${row.roomName || ''} ${row.roomNum || ''}`

const columns = [
  {
    name: 'name',
    field: (row: BoqWork) => row.rate.name,
    label: 'Работа',
    align: 'left' as const,
    sortable: true,
  },
  {
    name: 'unitOfMeasure',
    field: (row: BoqWork) => uomDescription(row.rate.unitOfMeasure),
    label: 'Ед. изм.',
    align: 'left' as const,
    sortable: true,
  },
  {
    name: 'volume',
    field: (row: BoqWork) => row.volume,
    label: 'Объем',
    align: 'left' as const,
    sortable: true,
  },
].concat(
  props.showLocation
    ? [
      {
        name: 'location',
        field: (row: BoqWork) => locationName(row),
        label: 'Локация',
        align: 'left' as const,
        sortable: true,
      },
    ]
    : [],
)
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
