<template>
  <div class="row q-col-gutter-sm">
    <div class="col-md-8 col-xs-12">
      <q-card flat bordered>
        <q-tabs v-model="currentTab" no-caps align="left" active-color="primary" mobile-arrows>
          <q-tab name="floor" label="Пол" />
          <q-tab name="ceil" label="Потолок" />
          <q-tab name="interior-door" label="Двери" />
          <q-tab name="window" label="Окна" />
          <q-tab name="wall" label="Стены" />
          <q-tab name="column" label="Колонны" />
          <q-tab name="stairway" label="Лестницы" />
          <q-tab name="supporting" label="Дополнительные работы" />
        </q-tabs>
      </q-card>
      <q-card class="q-mt-xs">
        <div class="q-pa-sm row justify-end" v-if="$q.screen.lt.md">
          <q-btn outline icon-right="menu" label="Работы" @click="leftDrawerOpen = true" />
        </div>
        <q-tab-panels v-model="currentTab">
          <q-tab-panel name="floor">
            <BoqFloor :floor="location!.floor" />
          </q-tab-panel>
          <q-tab-panel name="ceil">
            <BoqCeil :ceil="location!.ceil" />
          </q-tab-panel>
          <q-tab-panel name="interior-door">
            <BoqInteriorDoors :interior-doors="location!.interiorDoors" />
          </q-tab-panel>
          <q-tab-panel name="window">
            <BoqWindows :windows="location!.windows" />
          </q-tab-panel>
          <q-tab-panel name="wall">
            <BoqWallSections :wall-sections="location!.wallSections" />
          </q-tab-panel>
          <q-tab-panel name="column">
            <BoqFixedAssets :fixed-assets="columns" prefix="Колонна" />
          </q-tab-panel>
          <q-tab-panel name="stairway">
            <BoqFixedAssets :fixed-assets="stairways" prefix="Лестница" />
          </q-tab-panel>
          <q-tab-panel name="supporting">
            <BoqSupportingWorks :location-id="location!.id" />
          </q-tab-panel>
        </q-tab-panels>
      </q-card>
    </div>
    <div class="col-4" v-if="$q.screen.gt.sm">
      <WorkTable
        :fetchingWorks="fetchingWorks"
        :works="locationWorks"
        :editable="true"
        :show-location="false"
        :flat="false"
        :work-params-type="workParamsType"
      />
    </div>
    <q-drawer
      v-if="$q.screen.lt.md"
      v-model="leftDrawerOpen"
      side="right"
      bordered
      :width="350"
      overlay
      behavior="mobile"
    >
      <WorkTable
        :fetchingWorks="fetchingWorks"
        :works="locationWorks"
        :editable="true"
        :show-location="false"
        :flat="false"
        :work-params-type="workParamsType"
      />
    </q-drawer>
  </div>
</template>
<script lang="ts" setup>
import { storeToRefs } from 'pinia'
import { computed, ComputedRef, ref } from 'vue'
import { useBoqLocationStore } from '../../stores/boq-location-store'
import { useBoqWorkStore } from '../../stores/boq-work-store'
import WorkTable from '../WorkTable.vue'
import BoqFloor from './BoqFloor.vue'
import BoqCeil from './BoqCeil.vue'
import BoqInteriorDoors from './BoqInteriorDoors.vue'
import BoqWindows from './BoqWindows.vue'
import BoqWallSections from './BoqWallSections.vue'
import BoqSupportingWorks from './BoqSupportingWorks.vue'
import BoqFixedAssets from './BoqFixedAssets.vue'
import { FixedAssetType } from '../../api/fixed-asset/types'
import { ParamsType } from 'src/features/lookup/rate/types'

const { location } = storeToRefs(useBoqLocationStore())
const { works, fetchingWorks } = storeToRefs(useBoqWorkStore())

const currentTab = ref<string>('floor')

const workParamsType: ComputedRef<ParamsType[]> = computed(() => {
  switch(currentTab.value) {
    case 'floor': return ['FLOOR', 'FLOOR_SECTION']
    case 'ceil': return ['CEIL', 'CEIL_SECTION']
    case 'interior-door': return ['DOOR']
    case 'window': return ['WINDOW']
    case 'wall': return ['WALL_SECTION']
    case 'column': return ['FIXED_ASSET']
    case 'stairway': return ['FIXED_ASSET']
    case 'supporting': return ['SUPPORTING']
    default: return []
  }
})

const filteredFixedAssets = (type: FixedAssetType) => () => {
  return location.value!!.fixedAssets.filter((item) => item.type == type)
}

const columns = computed(filteredFixedAssets('COLUMN'))
const stairways = computed(filteredFixedAssets('STAIRWAY'))

const locationWorks = computed(() => {
  return works.value.filter((item) => item.locationId == location.value!.id)
})

const leftDrawerOpen = ref(false)
</script>
