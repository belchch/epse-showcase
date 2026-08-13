<template>
  <q-dialog v-model="open">
    <q-card style="width: 100%;max-width: 800px;">
      <q-card-section class="row items-center">
        <div class="text-h6">Дополнительные работы</div>
        <q-space />
        <q-btn icon="close" flat round dense v-close-popup />
      </q-card-section>
      <q-card-section class="q-pl-none">
        <div class="text-center" v-if="loading">
          <q-spinner color="primary" size="3em" :thickness="2" />
          <div class="q-mt-md">Загрузка...</div>
        </div>
        <q-list v-else>
          <q-item v-if="works.length == 0">
            <q-item-section class="text-center">
              Список пуст
            </q-item-section>
          </q-item>
          <q-item v-for="(item, index) in works" :key="index">
            <q-item-section>
              <q-toggle size="sm" color="secondary" @update:model-value="(val) => updateVisible(item, val)"
                :label="`${item.rate.name}`" v-model="item.visible" />
            </q-item-section>
          </q-item>
        </q-list>
      </q-card-section>
      <q-card-actions align="right">
        <q-btn flat label="Закрыть" color="primary" v-close-popup />
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-btn @click="openDialog" outline color="secondary" size="sm" label="Доп. работы" class="q-ml-md" />
</template>
<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { useBoqCommonWorkService } from 'src/features/boq/composables/boq-common-work';
import { BoqWork } from '../api/types';
import { useBoqWorkService } from 'src/features/boq/composables/boq-work';
import { useBoqStore } from '../stores/boq-store';
import { storeToRefs } from 'pinia';
import { useEstimateService } from 'src/features/estimate/composables/estimate-service'
const open = ref<boolean>(false)
const { requestWorks, updateWork } = useBoqCommonWorkService();
const {boq} = storeToRefs(useBoqStore())
const works = ref<BoqWork[]>([]);
const boqService = useBoqWorkService()
const estimateService = useEstimateService()
const openDialog = async () => {
  open.value = true
  loading.value = true
  works.value = await requestWorks();
  loading.value = false
};
const loading = ref<boolean>(false)
const updateVisible = async (item: BoqWork, val: boolean) => {
  item.visible = val;
  try {
    await updateWork(item);
    await boqService.requestWorks()
    await estimateService.getEstimate();
  } catch {
    item.visible = !val;
  }
}
</script>
