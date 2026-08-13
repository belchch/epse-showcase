<template>
  <div class="row q-col-gutter-sm">
    <div class="col-md-7 col-xs-12">
      <BoqLocations
        @navigate-location="(location: BoqLocation) => navigateLocation(location.id)"
        @openDrawer="openDrawer"
      />
      <EstimateTabs />
    </div>
    <div class="col-5" v-if="$q.screen.gt.sm">
      <BoqResultItems />
    </div>
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
    <BoqResultItems />
  </q-drawer>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import { BoqLocation } from '../api/types'
import { useBoqLocationService } from '../composables/boq-location'

import BoqLocations from './BoqLocations.vue'
import BoqResultItems from './BoqResultItems.vue'
import EstimateTabs from 'src/features/estimate/components/EstimateTabs.vue'

const { navigateLocation } = useBoqLocationService()

const leftDrawerOpen = ref(true)

const openDrawer = () => {
  leftDrawerOpen.value = true
}
</script>
