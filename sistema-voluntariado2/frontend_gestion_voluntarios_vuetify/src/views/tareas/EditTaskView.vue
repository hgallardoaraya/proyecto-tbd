<template>
    <v-container>
        <v-container class="d-flex flex-column align-center">
            <v-alert v-for="(msg, i) in Object.values(messages)" :key="i" v-if="error" class="my-2" width="500px" type="error">{{ `${i + 1}. ${msg}` }}</v-alert>
            <v-alert v-if="success" class="my-2" type="success" width="500px"><ul><li v-for="(msg, i) in Object.values(messages)" :key="i">{{ `${i + 1}. ${msg}` }}</li></ul></v-alert>
            <v-sheet width="500px" class="my-5">
                <v-btn color="terciary" variant="outlined" @click="$router.push('/tasks')">Volver</v-btn>
            </v-sheet>
            <v-card width="500px" class="pa-5 text-center">
                <p class="text-h4 mb-5">Editar tarea</p>
                <v-divider></v-divider>
                <v-form @submit.prevent="submitEditTaskForm" class="my-5">    

                <v-select
                    v-model="idEmergencia"
                    prepend-inner-icon="mdi-account-wrench-outline"
                    :items="items"
                    item-title="label"
                    item-value="value"                    
                    label="Emergencia"
                    variant="outlined"
                    required
                ></v-select>
                
                <v-text-field
                    v-model="task.nombre"                                                       
                    prepend-inner-icon="mdi-email-outline"
                    label="Nombre"                    
                    variant="outlined"
                    required
                ></v-text-field>

                <v-text-field
                    v-model="task.voluntariosInscritos"                                                       
                    prepend-inner-icon="mdi-lock-outline"
                    label="Voluntarios inscritos"                    
                    type="number"
                    variant="outlined"
                    required
                ></v-text-field>                            

                <v-text-field
                    v-model="task.voluntariosRequeridos"                                                       
                    prepend-inner-icon="mdi-lock-outline"
                    label="Voluntarios requeridos"                    
                    type="number"
                    variant="outlined"
                    required
                ></v-text-field>                
                
                <v-text-field
                    v-model="task.fechaInicio"   
                    prepend-inner-icon="mdi-calendar-start-outline"                                                                                     
                    label="Fecha inicio"                                                         
                    readonly
                    variant="outlined"
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
                    v-model="task.fechaFin"   
                    prepend-inner-icon="mdi-calendar-end-outline"                                                                                     
                    label="Fecha fin"                                                         
                    readonly
                    variant="outlined"
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
                    v-model="idEstado"
                    prepend-inner-icon="mdi-cached"
                    :items="estadoItems"
                    item-title="label"
                    item-value="value"                    
                    label="Estado"
                    variant="outlined"
                    required
                ></v-select>

                <v-select
                    v-model="idsHabilidades"
                    chips
                    prepend-inner-icon="mdi-tools"
                    label="Habilidades"
                    :items="emergencias.find(e => e.id == idEmergencia)?.habilidades"
                    item-title="descripcion"
                    item-value="id"   
                    variant="outlined"
                    multiple                    
                ></v-select>   

                <v-textarea 
                    v-model="task.descripcion"
                    prepend-inner-icon="mdi-text-long"    
                    variant="outlined"
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
            
        </v-container>
    </v-container>
</template>

<script lang="ts" setup>
import { ref, onMounted, watch } from 'vue'
import Tarea from "@/interfaces/Tarea"
import Emergencia from '@/interfaces/Emergencia'
import {useAuth} from '@/store/auth'
import { getEmergencias } from '@/services/EmergenciaService'
import { updateTask, getTask } from '@/services/TareaService'
import { getEstados } from '@/services/EstadoService';
import { useRoute, useRouter } from 'vue-router'
import L from 'leaflet'

const route = useRoute();
const router = useRouter();
const auth = useAuth();
const selectedDate = ref<Date|null>(null);
const selectedDate2 = ref<Date|null>(null);
const menu = ref<boolean>(false);
const menu2 = ref<boolean>(false);
const task = ref<Tarea>({
    id: undefined,
    nombre: "",
    descripcion: "",
    voluntariosRequeridos: undefined,    
    voluntariosInscritos: undefined,
    estado: undefined,
    fechaInicio: "",
    fechaFin: "",
    latit: 0,
    longit: 0
});
const estadoItems = ref<object[]>([]);
const idEstado = ref<number|undefined>(undefined);
const idEmergencia = ref<number|undefined>(undefined);
const items = ref<object[]>([])
const idsHabilidades = ref<number[]|undefined>(undefined);
const emergencias = ref<Emergencia[]>([]);
const marker = ref();

const show = ref<boolean>(false);
const error = ref<boolean>(false);
const success = ref<boolean>(false);
const messages = ref<object>({});

onMounted(async () => {
    emergencias.value = await getEmergencias(auth.token || "");    
    emergencias.value.map(emergencia => 
        items.value?.push({
            value: emergencia.id,
            label: emergencia.nombre
        })
    )
    
    task.value = await getTask(auth.token || "", parseInt(`${route.params.id}`) ?? -1);
    idEmergencia.value = task.value.emergencia?.id;     
    idsHabilidades.value = task.value.habilidades ? [] : undefined;     
    task.value.habilidades?.map(habilidad => {        
        if(habilidad.id){
            console.log("Habilidad ", habilidad);
            idsHabilidades.value?.push(habilidad.id);
        }       
    })    

    const estados = await getEstados(auth.token || "");
    estados.map(estado => 
        estadoItems.value?.push({
            value: estado.id,
            label: estado.descripcion
        })
    )

    idEstado.value = task.value.estado?.id;    
    console.log(task.value);

    let map = L.map('map').setView([task.value.latit || -33.45694, task.value.longit || -70.64827], 3);
                
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 20,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);

    marker.value = L.marker([task.value.latit || -33.45694, task.value.longit || -70.64827]).addTo(map);
    marker.value.options.riseOnHover = false;
    
    map.on('click', (e) => {
        const latLng = e.latlng;
        
        if(marker.value){
            map.removeLayer(marker.value);
        }
        marker.value = L.marker([latLng.lat, latLng.lng]).addTo(map);
        marker.value.options.riseOnHover = false;
        map.addLayer(marker.value);

        task.value.latit = latLng.lat;
        task.value.longit = latLng.lng;            
});
})

const submitEditTaskForm = async () => {
    if(idsHabilidades == null) return;
    const response = await updateTask(task.value, idEmergencia.value ?? -1, idEstado.value ?? -1, auth.token || "", idsHabilidades.value ?? []);    
    if(response.status == 200){
        success.value = true;
        error.value = false;
        alert("Tarea editada con éxito");
        router.push({name: "tasks"});
    }else{        
        error.value = true;
        success.value = false;
        messages.value = response.messages || "Hubo un error al crear la tarea, intente nuevamente.";            
    }
}

watch(selectedDate, (newSelectedDate) => {
    task.value.fechaInicio = newSelectedDate?.toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' }).split('/').join('-');

    menu.value = false;
})

watch(selectedDate2, (newSelectedDate) => {
    task.value.fechaFin = newSelectedDate?.toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' }).split('/').join('-');;
    menu2.value = false;
})

</script>

<style scoped>
</style>