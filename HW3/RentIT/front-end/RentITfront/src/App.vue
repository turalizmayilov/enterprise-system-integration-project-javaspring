<template>
  <div id="app">
    <h2> RentIT </h2>
    <h2> Lists of purchase Order</h2>
    <table class="table is-table-bordered is-table-striped is-fullwidth">
    <thead>
        <tr>
            <th class="has-text-center">id</th>
            <th class="has-text-center">Name</th>
            <th class="has-text-center">Start Date</th>
            <th class="has-text-center">End Date</th>
            <th class="has-text-center">Site Engineer</th>
            <th class="has-text-center">Price</th>
            <th class="has-text-center">Actions</th>
            <th class="has-text-center">Actions</th>
        </tr>
    </thead>
    <tbody>
          <tr class="table-row-rentit" v-for="pending in allrequest" :key="pending._id" >
            <td class="has-text-center" id="name"> {{pending._id}}</td>
            <td id = "plantNameWE2" class="has-text-center"> {{pending.plant.name}}</td>
            <td id = "plantStartDateWE2" class="has-text-center"> {{pending.rentalPeriod.startDate}}</td>
            <td id = "plantEndDateWE2" class="has-text-center"> {{pending.rentalPeriod.endDate}}</td>
            <td class="has-text-center"> {{pending.requestingSiteEngineer.firstName}} {{pending.requestingSiteEngineer.lastName}} </td>
            <td id = "plantPriceWE2" class="has-text-center"> {{pending.rentalCost.total}}</td>
            <td><a v-on:click="accept" class="button is-success is-outlined">Accept</a> </td>
            <td> <a v-on:click="reject" @click="isActive = !isActive" class="button is-danger is-outlined">Reject</a> </td>
        </tr>
    </tbody>
</table>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'App',

  data: function(){
      return{
        allrequest: [],
        rejectedRequest:{},
      }
  },
   mounted:function(){
    this.pendingpurchaseOrder();
  },
  methods: {
      pendingpurchaseOrder: function(){
         axios.get(" http://localhost:8090/api/sales/orders")
        .then(response => {
          this.allrequest = response.data._embedded.plantHireRequestDTOList;
          console.log("Response", this.allrequest);
        });
      },
      accept: function(inputOrder){
           let i= document.getElementById("name").innerHTML
           let params = i + "/accept"
           console.log("Accept", i);
        axios.get("http://localhost:8080/api/requests/", params)
        .then(response => {
           this.$snackbar.open("You have accepted this purchase order.");
         console.log("Accept", response.data._embedded);
        });
      },
      reject: function(inputOrder){
           let i= document.getElementById("name").innerHTML
           let params = i + "/reject"
           console.log("Reject", i);
        axios.get("http://localhost:8080/api/requests/", params)
        .then(response => {
          this.$snackbar.open("You have rejected this purchase order.");
          console.log("Reject", response.data._embedded.plantHireRequestDTOList);
          this.rejectedRequest = response.data._embedded.plantHireRequestDTOList;
        });
      },
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
