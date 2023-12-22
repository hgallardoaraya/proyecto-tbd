<template>
    <div class="d-flex flex-column align-center">
        <v-card width="500px" class="pa-5 my-7 text-center">
            <p class="text-h4 mb-5">Ubicación tareas</p>
            <v-divider></v-divider>
            <v-form class="my-5" @submit.prevent="submitGetTasksRegion">
                <v-select
                    v-model="nombreRegion"
                    prepend-inner-icon="mdi-account-wrench-outline"            
                    :items="items"
                    item-title="label"
                    item-value="value"                    
                    label="Region"
                    required
                ></v-select>
                <v-btn
                color="primary"
                class="w-100"
                type="submit"
                size="large"
                >
                    Buscar
                </v-btn>
            </v-form>
        </v-card>
        <div id="map"></div>
    </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { getTasksRegion } from '@/services/TareaService';
import L from 'leaflet'

const nombreRegion = ref<string>(""); 
const regiones = [
        "Región del Libertador Bernardo O'Higgins",
        "Región de Aysén del Gral.Ibañez del Campo",
        "Región de Valparaíso",
        "Región de Arica y Parinacota",
        "Región de Antofagasta",
        "Región de Los Lagos",
        "Región de Atacama",
        "Región de La Araucanía",
        "Región de Tarapacá",
        "Región de Coquimbo",
        "Zona sin demarcar",
        "Región de Los Ríos",
        "Región del Maule",
        "Región de Magallanes y Antártica Chilena",
        "Región Metropolitana de Santiago",
        "Región del Bío-Bío"
]
const items:Object[] = [];

regiones.map(nombreRegion => {
        items.push({
            value: nombreRegion,
            label: nombreRegion
        })
    })


let map = ref();

const submitGetTasksRegion = async () => {
    if(nombreRegion && map.value){
        const tareas = await getTasksRegion(nombreRegion.value);
        if(tareas){
            const markers = new Array(tareas.length);
    
            for(let i = 0; i < tareas.length; i++){         
                let tarea = tareas[i]; 
            
                if(tarea.longit && tarea.latit){
                    markers[i] = L.marker([tarea.latit, tarea.longit]).addTo(map.value);
                    markers[i].bindPopup(`${tarea.nombre}`).openPopup();
                    markers[i].options.riseOnHover = false;
                }
            }
        }
    }
}

onMounted(() => {    
    map.value = L.map('map').setView([-33.45694, -70.64827], 5);

    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map.value);

})
</script>

<style scoped>
#map{
    width: 500px;
    height: 500px;
}
</style>