<template>
    <v-container class="d-flex flex-column justify-center align-center">
        <h1>Tareas</h1>
        <v-container>
            <v-data-table
                v-if="items"
                v-model:page="page"
                :headers="headers"
                :items="items"
                :pagination.sync="pagination"
                :items-per-page="itemsPerPage"
                :server-items-length="totalItems"
                hide-default-footer
                :hide-items-per-page="true"
                class="elevation-1"
                hover            
                @update:options="options = $event"
                @update:page="updatePage"
                @update:items-per-page="updateItemsPerPage"
            >                    
                <template v-slot:item.acciones="{ item }">                    
                    <v-btn icon="mdi-eye-outline" variant="text" @click="$router.push(`/tasks/${item.value}`)"></v-btn>
                    <v-btn icon="mdi-pencil-outline" variant="text" @click="$router.push(`/tasks/${item.value}/edit`)"></v-btn>
                    <v-btn icon="mdi-delete-outline" variant="text" @click="deleteTask(item.value)"></v-btn>
                </template>
            </v-data-table>
        </v-container>
        <router-link to="/tasks/create">
            <v-btn color="primary">Crear tarea</v-btn>
        </router-link>
    </v-container>
</template>

<script lang="ts" setup>
import { getTasks } from '@/services/TareaService';
import { deleteTask } from '@/services/TareaService';
import { computed } from 'vue';
import { onMounted } from 'vue';
import { ref } from 'vue';
import { VDataTable } from 'vuetify/labs/VDataTable'
import { useAuth } from '@/store/auth';
import Tarea from '@/interfaces/Tarea';

const auth = useAuth();

interface DataTableHeader {
    title: string,
    key: string,
    align?: string
    sortable?: boolean
}

interface Options {
    pageCount: number
}

const options = ref<Options>({
    pageCount: 1
})
const page = ref<number>(1);
const itemsPerPage = ref<number>(10);
const pagination = ref({
    sortBy: 'name',
    descending: false
})
const headers = ref<DataTableHeader[]>([
    { title: 'Nombre', key: 'nombre' },
    { title: 'Voluntarios', key: 'voluntarios', sortable: false, align: 'center'},
    { title: 'Fecha inicio', key: 'fechaInicio', sortable: false, align: 'center' },
    { title: 'Fecha fin', key: 'fechaFin', sortable: false, align: 'center' },
    { title: 'Estado', key: 'estado', sortable: false, align: 'center' },
    { title: 'Acciones', key: 'acciones', sortable: false, align: 'center'}
])
const items = ref<object[]>([])
const totalItems = computed(() => items.value.length);

const updatePage = (newPage:number) => {
    page.value = newPage;
}

const updateItemsPerPage = (newItemsPerPage:number) => {
    itemsPerPage.value = newItemsPerPage;
}

onMounted(async () => {    
    const tareas = await getTasks(auth.token || '');
    if(tareas){
        tareas.map((tarea:Tarea) => {
            items.value.push({
                nombre: tarea.nombre,
                voluntarios: `${tarea.voluntariosInscritos}/${tarea.voluntariosRequeridos}`,
                fechaInicio: tarea.fechaInicio,
                fechaFin: tarea.fechaFin,
                estado: tarea.estado?.descripcion,
                id: tarea.id
            })
        })
    }
})

</script>

<style scoped>

</style>