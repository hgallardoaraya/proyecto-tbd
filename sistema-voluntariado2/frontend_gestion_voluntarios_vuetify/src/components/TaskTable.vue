<template>
    <table class="table table-hover w-75">
        <thead>
            <tr>
                <th class="text-primary" v-for="(field, i) in fields" scope="col" :key="i">
                    <span style="text-align: left; display: block;" v-if="field?.text != 'Acciones'">{{ field?.text }}</span>
                    <span style="text-align: center;" v-if="field?.text == 'Acciones'">{{ field?.text }}</span>
                </th>                
            </tr>
        </thead>
        <tbody>
            <tr v-for="(tarea, i) in tareas" :key="i">
                <td style="text-align: left;">{{ tarea.nombre }}</td>
                <td style="text-align: left;">{{ `${tarea.voluntariosInscritos}/${tarea.voluntariosRequeridos}` }}</td>
                <td style="text-align: left;">{{ tarea.fechaInicio }}</td>
                <td style="text-align: left;">{{ tarea.fechaFin }}</td>
                <td style="text-align: left;">{{ tarea.estado?.descripcion }}</td>
                <td><ThreeDots/></td>
            </tr>   
        </tbody>
    </table>
    <nav>
        <ul class="pagination">
            <li class="page-item">
                <a class="page-link" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <li class="page-item" @click="changePage"><a class="page-link" :id="'anchor' + (currentPage - 1).toString()">{{ currentPage - 1 }}</a></li>
            <li class="page-item"  @click="changePage"><a class="page-link" :id="'anchor' + (currentPage).toString()">{{ currentPage }}</a></li>
            <li class="page-item"  @click="changePage"><a class="page-link" :id="'anchor' + (currentPage + 1).toString()">{{ currentPage + 1 }}</a></li>
            <li class="page-item">
                <a class="page-link" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>                
                </a>
            </li>
        </ul>
    </nav>
</template>

<script lang="ts" setup>
import Field from '@/interfaces/Field';
import Tarea from '@/interfaces/Tarea';
import ThreeDots from './ThreeDots.vue';
import { computed, defineProps, ref } from 'vue';


interface Props {
    fields: Field[],
    tareas: Tarea[]
}

const props = defineProps<Props>();
const currentPage = ref<number>(2);
const perPage = ref<number>(10);
const rows = computed(() => props.tareas.length);

const changePage = (e:Event) => {    
    const anchor = (e.target as HTMLElement);
    const parentLi = (e.target as HTMLElement).parentElement;    
    const prevAnchor = document.getElementById(`anchor${currentPage.value}`);
    const prevParentLi = prevAnchor?.parentElement;
    currentPage.value = parseInt(anchor.innerHTML);
    prevParentLi?.classList.remove('active');
    parentLi?.classList.add('active');
    console.log(currentPage.value);
}

</script>

<style scoped>

</style>