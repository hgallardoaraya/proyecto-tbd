<template>
<v-container class="d-flex align-center h-screen"> 
    <v-container class="d-flex flex-column align-center">
        <v-alert v-for="(msg, i) in Object.values(messages)" :key="i" v-if="error" class="my-2" width="500px" type="error">{{ `${i + 1}. ${msg}` }}</v-alert>
        <v-alert v-if="success" class="my-2" type="success" width="500px"><ul><li v-for="(msg, i) in Object.values(messages)" :key="i">{{ `${i + 1}. ${msg}` }}</li></ul></v-alert>
        
        <v-card width="500px" class="mt-7 pa-5 text-center">
            <p class="text-h4 mb-5">Iniciar sesión</p>
            <v-divider ></v-divider>
            <v-form @submit.prevent="submitLoginForm" v-model="valid" class="my-5">    
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

                <v-select 
                    class="mb-7"
                    v-model="idRol"
                    prepend-inner-icon="mdi-account-wrench-outline"
                    :items="items"
                    item-title="label"
                    item-value="value"
                    label="Tipo de usuario"
                    variant="outlined"
                    required
                ></v-select>

                <v-btn
                    color="primary"
                    class="w-100 mb-5"
                    type="submit"
                    size="large"
                >
                    Ingresar
                </v-btn>

                <v-btn                           
                    class="w-100 mb-5"
                    type="submit"                    
                    size="large"        
                    @click="$router.push('/register')"
                >
                    Registrarse
                </v-btn>

                <router-link :to="{ name: 'login'}">
                    ¿Olvidaste tu contraseña?
                </router-link>

            </v-form>
        </v-card>
    </v-container>
</v-container>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Usuario from "@/interfaces/Usuario"
import { useAuth } from '@/store/auth';
import router from '@/router';


const store = useAuth();
const valid = ref<boolean>(false);
const idRol = ref<number|undefined>(undefined);
const items = ref<object[]>([
    {value: 2, label: "Voluntario"},
    {value: 1, label: "Coordinador"},
]);
const user = ref<Usuario>({
    email: "",
    password: ""
});
const error = ref<boolean>(false);
const success = ref<boolean>(false);
const messages = ref<object>({});


const submitLoginForm = async () => {
    const response = await store.login(user.value.email ?? '', user.value.password ?? '', idRol.value ?? -1);
    console.log(response);
    if(response.status == 200){
        success.value = true;
        error.value = false;
        messages.value = response.messages || "Inicio de sesión exitoso.";    
        router.push({name: "tasks"});
    }else{     
        error.value = true;
        success.value = false;
        messages.value = response.messages || "Hubo un error al iniciar sesión, intente nuevamente.";            
    }
}
</script>


<style scoped>

</style>