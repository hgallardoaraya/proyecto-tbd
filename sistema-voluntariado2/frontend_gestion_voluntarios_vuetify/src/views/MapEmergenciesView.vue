<template>
<div>
    <div class="d-flex flex-column align-center">
        <v-card width="500px" class="pa-5 my-7 text-center">
            <p class="text-h4 mb-5">Ubicación voluntarios</p>
            <v-divider></v-divider>
            <v-form class="my-5" @submit.prevent="submitGetEmergenciesVolunteers">
                <v-select
                    v-model="idEmergencia"
                    prepend-inner-icon="mdi-account-wrench-outline"            
                    :items="items"
                    item-title="label"
                    item-value="value"                    
                    label="Emergencia"
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
</div>
</template>

<script lang="ts" setup>        
import { onMounted, ref } from 'vue';
import { getEmergencias } from '@/services/EmergenciaService';
import { getVoluntariosEmergencia } from '@/services/EmergenciaService';
import { useAuth } from '@/store/auth';
import Emergencia from '@/interfaces/Emergencia';
import Usuario from '@/interfaces/Usuario';
import L from 'leaflet'

const idEmergencia = ref<number|undefined>(undefined);
const items = ref<object[]>([])
const emergencias = ref<Emergencia[]>([]);

onMounted(async () => {
    const auth = useAuth();
    emergencias.value = await getEmergencias(auth.token || "");    
    emergencias.value.map(emergencia => {
        items.value?.push({
            value: emergencia.id,
            label: emergencia.nombre
        })
    })
    console.log(emergencias);
})

const submitGetEmergenciesVolunteers = async () => {
    console.log(idEmergencia.value);
    if(idEmergencia.value == null) return;
    const voluntarios:Usuario[] = await getVoluntariosEmergencia(idEmergencia.value);
    console.log(voluntarios);
    const markers = new Array(voluntarios.length);

    if(voluntarios != null) {
        if(voluntarios[0].latit && voluntarios[0].longit){
            let map = L.map('map').setView([voluntarios[0].latit, voluntarios[0].longit], 3);
            
            L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
                maxZoom: 19,
                attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
            }).addTo(map);
            
            
            for(let i = 0; i < voluntarios.length; i++){         
                let voluntario = voluntarios[i]; 
                
                if(voluntario.longit && voluntario.latit){
                    markers[i] = L.marker([voluntario.latit, voluntario.longit]).addTo(map);
                    markers[i].bindPopup(`${voluntario.nombre} ${voluntario.apellido}`).openPopup();
                    markers[i].options.riseOnHover = false;
                }
            }   
        }        
    }
}

</script>

<style scoped>

</style>