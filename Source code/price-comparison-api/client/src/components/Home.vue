<template>
  <div class="container mt-5 pt-5 pb-5 search-box">
    <div class="row">
      <div class="col-8">
        <div class="row">
          <div class="col-8">
            <h2>Welcome back!! What are you looking for today.</h2>
          </div>
        </div>
        <div class="row">
          <div class="col">
            <h3>Get the best prices.</h3>
            <p>Compare prices from different shops</p>
          </div>
        </div>
        <!-- Search Input and button container -->
        <div class="row pt-3">
          <div class="col-7">
            <div class="input-group rounded">
              <!-- use v-model to handle user search input -->
              <input v-model="watchName" v-on:keyup.enter="search" type="search" class="form-control search-input" placeholder="Search products"
                aria-label="Search" aria-describedby="search-addon"/>
            </div>
          </div>
          <div class="col-5">
            <button v-if="watchName.length > 0" v-on:click="search" type="button" class="btn btn-primary">
              <i class="fa-solid fa-paper-plane"></i>
            </button>
            <button v-else disabled type="button" class="btn btn-primary">
              <i class="fa-solid fa-paper-plane"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="col-2">
        <img class="watch-image img-fluid" src="@/Images/watch.png" alt="Watch Image">
      </div>
    </div>
  </div>
  <!-- most popular brands container -->
  <div class="container pt-3 pb-3">
    <h3 class="text1">Most Popular Brands</h3>
  </div>
  <div class="container">
    <div class="row">
      <div class="col-3 p-3 m-5 image-con">
        <img class="brand-image" src="@/Images/casio logo.png" alt="Casio Logo">
      </div>
      <div class="col-3 p-3 m-5 image-con">
        <img class="brand-image" src="@/Images/guess logo.png" alt="Guess Logo">
      </div>
      <div class="col-3 p-3 m-5 image-con">
        <img class="brand-image" src="@/Images/seiko logo.png" alt="Seiko Logo">
      </div>
    </div>
  </div>
</template>


<script>
export default {
  name: 'HomePage',
  data() {
    return {
      watchName: '',
    }
  },

  methods: {
    //method to handle the search functionality
    search() {
      //dynamically import router and push to results page
      import('@/main').then(({ default: router }) => {
            router.push({ name: 'results', params: { watchName: this.watchName } });
      });

    },
  },
}
</script>

