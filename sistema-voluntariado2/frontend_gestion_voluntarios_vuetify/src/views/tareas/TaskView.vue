<template>
    <v-container class="d-flex flex-column align-center">
        <v-sheet width="500px" class="my-5">
            <v-btn color="terciary" variant="outlined" @click="$router.push({ name: 'tasks' })">Volver</v-btn>
        </v-sheet>
        <v-card width="500px" class="pa-5">            
            <v-card-item>                                          
                <v-chip color="{{ getStateColor(task.estado?.descripcion) }}" text-color="white" class="mb-3">
                    {{ task.estado?.descripcion }}
                </v-chip>  
                <v-card-title>{{ task.nombre }}</v-card-title>                                
            </v-card-item>            
            <v-card-item>                    
                <p class="text-subtitle-2"><v-icon icon="mdi-card-text-outline" class="mr-2"></v-icon>Descripción</p>
            </v-card-item>            
            <v-divider></v-divider>
            <v-card-item>
                <div class="d-flex">                   
                    <p>
                        {{ task.descripcion }}
                    </p>            
                </div>
            </v-card-item>
                        
            <v-card-item>                    
                <p class="text-subtitle-2"><v-icon icon="mdi-calendar-clock-outline" class="mr-2"></v-icon>Duración</p>
            </v-card-item>
            <v-divider></v-divider>
            <v-card-item>
                <div class="d-flex">                    
                    <p>{{ task.fechaInicio }} </p>&nbsp;<p class="font-weight-medium" >hasta</p>&nbsp;<p> {{ task.fechaFin }} </p>
                </div>
            </v-card-item>

            <v-card-item>                    
                <p class="text-subtitle-2"><v-icon icon="mdi-account-heart-outline" class="mr-2"></v-icon>Voluntarios</p>
            </v-card-item>
            <v-divider></v-divider>
            <v-card-item>
                <div class="d-flex">                    
                    <p>{{ task.voluntariosInscritos }}/{{ task.voluntariosRequeridos }} voluntarios</p>
                </div>
            </v-card-item>       
            
            <v-card-item>                    
                <p class="text-subtitle-2"><v-icon icon="mdi-card-text-outline" class="mr-2"></v-icon>Habilidades</p>
            </v-card-item>            
            <v-divider></v-divider>
            <v-card-item>
                <div>                   
                    <p v-for="(habilidad, i) in task.habilidades" :key="task.id" class="mb-2">{{ (i + 1) + ". " + habilidad.descripcion }}</p>
                </div>
            </v-card-item>

            <v-card-item>                    
                <p class="text-subtitle-2"><v-icon icon="mdi-map-outline" class="mr-2"></v-icon>Ubicación</p>
            </v-card-item>            
            <v-divider></v-divider>
            <v-card-item>
                <div id="map"></div>
            </v-card-item>
            <v-divider></v-divider>

            <v-card-actions>
                <div class="w-100 d-flex justify-end">
                    <v-btn color="primary" @click="$router.push(`/tasks/${route.params.id}/edit`)">
                        Editar
                    </v-btn>
                    <v-btn color="red" @click="deleteTask(Number(route.params.id))">
                        Eliminar
                    </v-btn>
                </div>
            </v-card-actions>
        </v-card>
    </v-container>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import Tarea from '@/interfaces/Tarea';
import { getTask, deleteTask } from '@/services/TareaService'
import { useAuth } from '@/store/auth';
import { useRoute } from 'vue-router';
import { getStateColor } from '@/services/EstadoService'
import L from 'leaflet'

const auth = useAuth();
const route = useRoute();
const task = ref<Tarea>({});
const marker = ref();

onMounted(async () => {
    task.value = await getTask(auth.token || '', parseInt(`${route.params.id}`) ?? -1)

    let map = L.map('map').setView([task.value.latit || -33.45694, task.value.longit || -70.64827], 3);
                
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 20,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);

    marker.value = L.marker([task.value.latit || -33.45694, task.value.longit || -70.64827]).addTo(map);
    marker.value.options.riseOnHover = false;    
})
</script>

<style scoped>

</style>