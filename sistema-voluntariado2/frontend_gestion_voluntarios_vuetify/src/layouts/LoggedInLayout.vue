<template>
  <v-container>
    <v-navigation-drawer
      expand-on-hover
      rail
    >
      <v-list>
        <v-list-item
        prepend-icon="mdi-account-circle-outline"		
        :title="`${nombre} ${apellido}`"
        :subtitle="email"
        
        ></v-list-item>
      </v-list>
      <v-divider></v-divider>
      <v-list density="compact" nav>
        <v-list-item prepend-icon="mdi-list-box-outline" title="Lista tareas" href="/tasks"></v-list-item>
        <v-list-item prepend-icon="mdi-earth" title="Ubicación tareas" href="/tasks/map"></v-list-item>
        <v-list-item prepend-icon="mdi-account-alert-outline" title="Ubicación voluntarios" href="/emergencies/map"></v-list-item>        
		<v-list-item prepend-icon="mdi-logout" title="Cerrar sesión" @click="auth.logout()"></v-list-item>        		
      </v-list>      
    </v-navigation-drawer>
    <router-view></router-view>
  </v-container>
</template>

<script lang="ts" setup>
import { useAuth } from '@/store/auth';
import { onMounted, ref } from 'vue';
import { getUsuario } from '@/services/UsuarioService'
const auth = useAuth();

const nombre = ref<String>("");
const apellido = ref<String>("");
const email = ref<String>("");

onMounted(async () => {
	const response = await getUsuario();
	nombre.value = response.nombre;
	apellido.value = response.apellido;
	email.value = response.email;
})

</script>

<style scoped>
</style>