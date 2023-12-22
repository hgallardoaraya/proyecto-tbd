<template>
  <v-app class="app">
    <component :is="$route.meta.layout">
      <RouterView/>
    </component>
  </v-app>
</template>

<script setup lang="ts">  

</script>

<style>
</style>