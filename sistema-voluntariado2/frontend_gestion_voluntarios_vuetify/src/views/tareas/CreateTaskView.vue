<template>
    <div class="d-flex flex-column align-center mb-10">
        <v-alert v-for="(msg, i) in Object.values(messages)" :key="i" v-if="error" class="my-2" width="500px" type="error">{{ `${i + 1}. ${msg}` }}</v-alert>
        <v-alert v-if="success" class="my-2" type="success" width="500px"><ul><li v-for="(msg, i) in Object.values(messages)" :key="i">{{ `${i + 1}. ${msg}` }}</li></ul></v-alert>
        <v-sheet width="500px" class="my-5">
            <v-btn color="terciary"  @click="$router.push('/tasks')">Volver</v-btn>
        </v-sheet>
        <v-card 
            width="500px" 
            class="pa-5 text-center"                
        >
            <p class="text-h4 mb-5">Crear tarea</p>
            <v-divider></v-divider>
            <v-form @submit.prevent="submitCreateTaskForm" class="my-5">    

            <v-select
                v-model="idEmergencia"
                prepend-inner-icon="mdi-account-wrench-outline"
                variant="outlined"
                :items="items"
                item-title="label"
                item-value="value"                    
                label="Emergencia"
                required
            ></v-select>
            
            <v-text-field
                v-model="tarea.nombre"                                                       
                prepend-inner-icon="mdi-email-outline"
                variant="outlined"
                label="Nombre"                    
                required
            ></v-text-field>

            <v-text-field
                v-model="tarea.voluntariosRequeridos"                                                       
                prepend-inner-icon="mdi-lock-outline"
                variant="outlined"
                label="Voluntarios requeridos"                    
                type="number"
                required
            ></v-text-field>                
            
            <v-text-field
                v-model="tarea.fechaInicio"   
                prepend-inner-icon="mdi-calendar-start-outline"                                                                                     
                variant="outlined"
                label="Fecha inicio"                                                         
                readonly
                required                            
            >            
                <v-menu                         
                    v-model="menu"
                    :close-on-content-click="false"
                    activator="parent">                        
                    <VDatePicker v-model="selectedDate"/>
                </v-menu>
            </v-text-field>

            <v-text-field
                v-model="tarea.fechaFin"   
                prepend-inner-icon="mdi-calendar-end-outline"                                                                                                     
                label="Fecha fin" 
                variant="outlined"                                                        
                readonly
                required                            
            >            
                <v-menu                         
                    v-model="menu2"
                    :close-on-content-click="false"
                    activator="parent">                        
                    <VDatePicker v-model="selectedDate2"/>
                </v-menu>
            </v-text-field> 
        
            <v-select
                v-model="idsHabilidades"
                chips
                variant="outlined"
                prepend-inner-icon="mdi-tools"
                label="Habilidades"
                :items="emergencias.find(e => e.id == idEmergencia)?.habilidades"
                item-title="descripcion"
                item-value="id"   
                multiple            
            ></v-select>            
                            
            <v-textarea 
                v-model="tarea.descripcion"
                variant="outlined"
                prepend-inner-icon="mdi-text-long"                    
                label="Descripción"                
            />

            <v-container class="text-left mb-7">
                <v-label class="mb-5">                    
                    <v-icon
                    icon="mdi-map-marker-account-outline"
                    ></v-icon>
                    Seleccionar ubicación
                </v-label>
                <div class="d-flex justify-center">
                    <div id="map"></div>
                </div>
            </v-container>

            <v-btn
                color="primary"
                class="w-100"
                type="submit"
                size="large"
            >
                Ingresar
            </v-btn>
            </v-form>
        </v-card>                        
    </div>
    
</template>

<script lang="ts" setup>
import { ref, onMounted, watch } from 'vue'
import Tarea from "@/interfaces/Tarea"
import {useAuth} from '@/store/auth'
import { getEmergencias } from '@/services/EmergenciaService'
import { createTask } from '@/services/TareaService'
import Emergencia from '@/interfaces/Emergencia'
import L from 'leaflet'
import icon from "leaflet/dist/images/marker-icon.png";
import iconShadow from "leaflet/dist/images/marker-shadow.png";
import { useRouter } from 'vue-router'

const router = useRouter();
const auth = useAuth();
const selectedDate = ref<Date|null>(null);
const selectedDate2 = ref<Date|null>(null);
const menu = ref<boolean>(false);
const menu2 = ref<boolean>(false);
const tarea = ref<Tarea>({
    nombre: "",
    descripcion: "",
    voluntariosRequeridos: undefined,    
    fechaInicio: "",
    fechaFin: "",
    latit: 0,
    longit: 0
});

const idsHabilidades = ref<number[]|undefined>(undefined);
const idEmergencia = ref<number|undefined>(undefined);
const items = ref<object[]>([])
const emergencias = ref<Emergencia[]>([]);
const marker = ref();

const show = ref<boolean>(false);
const error = ref<boolean>(false);
const success = ref<boolean>(false);
const messages = ref<object>({});

onMounted(async () => {
    let map = L.map('map').setView([-33.45694, -70.64827], 3);
                
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);

    marker.value = L.marker([-33.45694, -70.64827]).addTo(map);
    marker.value.options.riseOnHover = false;

    let DefaultIcon = L.icon({
        iconUrl: icon,
        shadowUrl: iconShadow,
        iconSize: [24, 36],
        iconAnchor: [12, 36],
    });

    map.on('click', (e) => {
        const latLng = e.latlng;
        
        if(marker.value){
            map.removeLayer(marker.value);
        }
        marker.value = L.marker([latLng.lat, latLng.lng]).addTo(map);
        marker.value.options.riseOnHover = false;
        map.addLayer(marker.value);

        tarea.value.latit = latLng.lat;
        tarea.value.longit = latLng.lng;
        console.log(latLng); // Aquí puedes hacer lo que desees con las coordenadas
    });

    emergencias.value = await getEmergencias(auth.token || "");    
    emergencias.value.map(emergencia => {
        items.value?.push({
            value: emergencia.id,
            label: emergencia.nombre
        })
    })
    console.log(emergencias);
})

const submitCreateTaskForm = async () => {
    console.log("Habilidades = ", idsHabilidades.value);
    const response = await createTask(tarea.value, idEmergencia.value ?? -1, idsHabilidades.value ?? [], auth.token || "");    
    if(response.status == 200){
        success.value = true;
        error.value = false;
        alert("Tarea creada con éxito");
        router.push({name: "tasks"});
    }else{        
        error.value = true;
        success.value = false;
        messages.value = response.messages || "Hubo un error al crear la tarea, intente nuevamente.";            
    }
}

watch(selectedDate, (newSelectedDate) => {
    tarea.value.fechaInicio = newSelectedDate?.toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' }).split('/').join('-');
;
    menu.value = false;
})

watch(selectedDate2, (newSelectedDate) => {
    tarea.value.fechaFin = newSelectedDate?.toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' }).split('/').join('-');;
    menu2.value = false;
})

</script>

<style scoped>
</style>