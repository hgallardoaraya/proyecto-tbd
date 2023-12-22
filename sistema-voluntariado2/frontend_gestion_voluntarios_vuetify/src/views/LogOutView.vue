<template>
<div>
  Cerrando sesión...
</div>
</template>

<script lang="ts" setup>
import { useRoute } from 'vue-router';
import { onMounted } from 'vue';
import { useAuth } from '@/store/auth';
const route = useRoute();

onMounted(() => {
    const auth = useAuth();
    auth.logout();
})

</script>

<style scoped>

</style>