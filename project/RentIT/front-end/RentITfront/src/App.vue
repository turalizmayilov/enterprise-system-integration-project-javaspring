<template>
  <div id="app">
    <router-link to="/">RentIt Employee</router-link> |
    <router-link to="/PlantDepotEmployee">Plant Depot Employee</router-link>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  font-size: 120%;
  margin-top: 60px;
}
.tabs.is-toggle ul {
    margin-top: 15px;
}
</style>
