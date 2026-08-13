<template>
    <q-dialog v-model="open">
        <q-card>
            <q-card-section>
                <div class="row q-gutter-md">
                    <template v-for="(url, index) in urls" :key="index">
                        <div class="photo">
                            <q-img :src="url" />
                        </div>
                    </template>
                </div>
            </q-card-section>
        </q-card>
    </q-dialog>
    <q-btn icon="image" color="secondary" @click="open = true" dense :disable="!urls?.length">
        <q-badge v-if="urls?.length" floating>{{ urls?.length }}</q-badge>
    </q-btn>
</template>
<script lang="ts" setup>
import { ref } from 'vue';

defineProps<{
    urls: string[] | undefined
}>()

const open = ref(false)
</script>
<style scoped>
.photo {
    width: 200px;
}
</style>