<template>
<v-container class="d-flex flex-column align-center">
    <v-sheet width="600px" class="my-5">
        <v-btn color="terciary" variant="outlined" @click="$router.push({ name: 'login' })">Volver</v-btn>
    </v-sheet>
    <v-container class="d-flex flex-column align-center w-75">
        <v-card width="600px" class="pa-5 text-center">
            <p class="text-h4 mb-5">Registrarse</p>
            <v-divider></v-divider>
            <div class="d-flex justify-center">
                <v-form @submit.prevent="submitRegisterForm" class="my-5 w-75">    
                        <v-text-field
                        v-model="user.nombre"                                                                       
                        prepend-inner-icon="mdi-account-outline"
                        label="Nombre"
                        variant="outlined"
                        required
                        ></v-text-field>
                        <v-text-field
                        v-model="user.apellido"        
                        prepend-inner-icon="mdi-label-outline"                                                               
                        label="Apellido"                
                        variant="outlined"
                        required
                        ></v-text-field>
                    <v-text-field
                        v-model="user.email"                                                                       
                        prepend-inner-icon="mdi-email-outline"
                        label="E-mail"
                        type="email"      
                        variant="outlined"          
                        required
                        ></v-text-field>
                        <v-text-field
                        v-model="user.password"    
                        prepend-inner-icon="mdi-lock-outline"                                                                   
                        label="Contraseña"    
                        type="password"   
                        variant="outlined"         
                        required
                        ></v-text-field>                
                        
                        <v-container class="text-left">
                            <v-label>                    
                                <v-icon
                                icon="mdi-account-wrench-outline"
                                ></v-icon>
                                Tipo de usuario
                            </v-label>
                            <div class="d-flex">
                                <v-checkbox                    
                                v-model="voluntario"
                                label="Voluntario"
                                color="primary"                        
                                ></v-checkbox>
                                <v-checkbox
                                v-model="coordinador"
                            label="Coordinador"
                            color="primary"
                            ></v-checkbox>
                        </div>                
                    </v-container>                        
                    <v-select
                    v-model="idInstitucion"
                    v-if="coordinador"
                    prepend-inner-icon="mdi-account-wrench-outline"
                    :items="items"
                    item-title="label"
                    item-value="value"                    
                    label="Institución"
                    variant="outlined"
                    required
                    ></v-select>
                    
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
                    type="submit"
                    size="large"
                    class="w-100"
                    >
                        REGISTRARSE
                    </v-btn>
                </v-form>
            </div>
        </v-card>
    </v-container>
</v-container>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import Usuario from "@/interfaces/Usuario"
import Institucion from '@/interfaces/Institucion';
import { getInstituciones } from '@/services/InstitucionService';
import { useAuth } from '@/store/auth';
import ErrorSVG from '@/components/ErrorSVG.vue'
import SuccessSVG from '@/components/SuccessSVG.vue'
import router from '@/router';
import L from 'leaflet'
import { Console } from 'console';

const store = useAuth();
const idInstitucion = ref<number|undefined>(undefined);
const items = ref<object[]>([]);
const user = ref<Usuario>({
    nombre: "",
    apellido: "",
    email: "",
    password: "",
    latit: 0,
    longit: 0
})
const voluntario = ref<boolean>(false);
const coordinador = ref<boolean>(false);
const error = ref<boolean>(false);
const success = ref<boolean>(false);
const messages = ref<object>({});
const marker = ref();

onMounted(async () => {
    let map = L.map('map').setView([-33.45694, -70.64827], 3);
                
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);

    map.on('click', (e) => {
        const latLng = e.latlng;
        
        if(marker.value){
            map.removeLayer(marker.value);
        }
        marker.value = L.marker([latLng.lat, latLng.lng]).addTo(map);
        marker.value.options.riseOnHover = false;
        
        user.value.latit = latLng.lat;
        user.value.longit = latLng.lng;
        console.log(latLng); // Aquí puedes hacer lo que desees con las coordenadas
    });

    const instituciones:Institucion[] = await getInstituciones();

    instituciones.map(institucion => 
        items.value.push({
            value: institucion.id, 
            label: institucion.nombre
        })
    )    

    // const success = (position:any) => {
    //     latit.value  = position.coords.latitude;
    //     longit.value = position.coords.longitude;

    //     console.log("coords", latit.value, longit.value);
    // };

    // const error = (err:any) => {
    //     console.log(err)
    // };

    // // This will open permission popup
    // navigator.geolocation.getCurrentPosition(success, error);
    console.log(items);
})


const submitRegisterForm = async () => {
    const response = await store.register(user.value.nombre ?? '', user.value.apellido ?? '', user.value.email ?? '', user.value.password ?? '', idInstitucion.value ?? -1, voluntario.value ?? false, coordinador.value ?? false, user.value.latit ?? 0, user.value.longit ?? 0);
    console.log(response);
    if(response.status == 200 ){
        success.value = true;
        error.value = false;
        messages.value = response.messages || "Usuario registrado con éxito.";
        router.push({name: "login"});
    }else{
        error.value = true;
        success.value = false;
        messages.value = response.messages || "Hubo un error al registrar al usuario, intente nuevamente.";            
    }
}
</script>

<style>
#map {
    height: 500px;
    width: 500px;
}
</style>