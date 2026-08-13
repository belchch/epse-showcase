<template>
    <card-section>
        <template v-for="work in works" :key="work.id">
            <div class="q-mt-md">
                <q-toggle color="secondary" v-model="work.visible" :label="work.rate.name" size="sm"
                    @update:model-value="updateWork(work)" />
            </div>
        </template>
    </card-section>
</template>
<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { BoqWork, toWorkUpdateRequest } from '../../api/types';
import { BoqSupportingWorkApi } from '../../api/boq-supporting-work-api';
import { BoqWorkApi } from '../../api/boq-work-api';
import { useBoqWorkService } from '../../composables/boq-work';

const { requestWorks } = useBoqWorkService()

const props = defineProps<{ locationId: number }>()

const works = ref<BoqWork[]>()

const updateWork = async (work: BoqWork) => {
    await BoqWorkApi.updateWork(work.id, toWorkUpdateRequest(work), false)
    await requestWorks()
}

onMounted(async () => {
    const response = await BoqSupportingWorkApi.getWorks(props.locationId)
    works.value = response.data
})
</script>