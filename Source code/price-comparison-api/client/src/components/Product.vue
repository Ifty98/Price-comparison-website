<template>
  <!-- product data container -->
  <div class="container mt-5 pt-5 pb-5 product-info">
    <div class="row">
      <div class="col-3 d-flex justify-content-center align-items-center">
        <img :src="image" class="product-image img-fluid" alt="watch image">
      </div>
      <!-- container for product details -->
      <div class="col-9">
        <div class="container">
          <div class="row">
            <div class="col-1">
              <p class="data-name">Name:</p>
            </div>
            <div class="col-10">
              <p>{{ name }}</p>
            </div>
          </div>
          <div class="row">
            <div class="col-1">
              <p class="data-name">Model:</p>
            </div>
            <div class="col-10">
              <p>{{ model }}</p>
            </div>
          </div>
          <div class="row">
            <div class="col-2">
              <p class="data-name">Colours:</p>
            </div>
            <!-- create buttons for each watch colour -->
            <div class="col-2" v-for="(colour, index) in colours" :key="index">
              <button class="colour-button" v-on:click="getWatch(colour)" type="button">{{ colour }}</button>
            </div>
          </div>
          <div class="row">
            <div class="col-2">
              <p class="data-name">Description:</p>
            </div>
            <div class="col-10">
              <p>{{ shortDescription(description) }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- web information section -->
  <!-- create a conatiner for each website where the watch is available -->
  <div class="container mt-3 pt-3 pb-3 web-info" v-for="(web, index) in webSites" :key="index">
    <div class="row">
      <h3>{{ web.name }}</h3>
    </div>
    <div class="row">
      <div class="col-5 mt-3">
        <!-- link to the website where the watch is located -->
        <a :href="web.url">Visit this website ==></a>
      </div>
      <div class="col-1 align-self-end">
        <p class="data-name">Price:</p>
      </div>
      <div class="col-1 align-self-end">
        <p>£{{ web.price }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ProductPage',
  data() {
    return {
      watchesByColours: [

      ],
      colours: [

      ],
      model: '',
      name: '',
      description: '',
      brand: '',
      image: '',
      webSites: [

      ],
    }
  },

  mounted() {
    //get product details on component mount
    this.getProduct();
  },

  methods: {
    //method to get product details using the watch ids
    getProduct() {
      //decode and parse watch ids from the route parameters
      const decodedIds = JSON.parse(decodeURIComponent(this.$route.params.ids));
      //make a request to the server to get the product details using the watch ids
      axios.get('http://localhost:8082/getSpecInfo?specIds=' + decodedIds)
        .then(response => {
          //store the data from the response
          this.watchesByColours = response.data.watchesByColours;
          //display the details of the first watch
          this.displayWatch(this.watchesByColours[0]);
          //extract and store colors from the fetched watches
          for (let index = 0; index < this.watchesByColours.length; index++) {
            this.colours[index] = this.watchesByColours[index].colour;
          }
        })
        .catch(error => {
          //handle any errors making the request to the server
          console.error('Error fetching data:', error);
        });
    },

    //method to display watch details 
    displayWatch(watch) {
      this.model = watch.model;
      this.name = watch.name;
      this.description = watch.description;
      this.brand = watch.brand;
      this.image = watch.image;
    },
    
    //method to get website information based on watch ids
    getWebSites(ids) {
      //make a request to the server to get the websites data using watch ids
      axios.get('http://localhost:8082/getInfoByColour?webIds=' + ids)
        .then(response => {
          //store the data from the response
          this.webSites = response.data.webInfo;
        })
        .catch(error => {
          //handle any errors making the request to the server
          console.error('Error fetching data:', error);
        });
    },
    
    //method to update details based on selected color
    getWatch(colour) {
      let watchIndex;
      //find the index of the watch with the selected color
      for (let index = 0; index < this.watchesByColours.length; index++) {
        if (this.watchesByColours[index].colour == colour) {
          watchIndex = index;
        }
      }
      this.model = this.watchesByColours[watchIndex].model;
      this.name = this.watchesByColours[watchIndex].name;
      this.description = this.watchesByColours[watchIndex].description;
      this.brand = this.watchesByColours[watchIndex].brand;
      this.image = this.watchesByColours[watchIndex].image;
      //get website information for the selected watch
      this.getWebSites(this.watchesByColours[watchIndex].watches);
    },

    //method to cut the description if it is too long
    shortDescription(description) {
      const maxLength = 400;
      return description.length > maxLength ? description.slice(0, maxLength) + '...' : description;
    },
  },
};
</script>