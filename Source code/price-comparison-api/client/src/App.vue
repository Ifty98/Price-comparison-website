<template>
  <div class="container mt-4">
        <h1>Watch Tracker</h1>
  </div>
  <!-- main content of the page displayed depending on the route -->
  <router-view></router-view>
  <!-- footer section -->
  <footer class="bg-dark text-center text-white mt-5">
    <div class="container p-4 pb-0">
        <section class="mb-4">
            <!-- Facebook -->
            <a class="btn btn-outline-light btn-floating m-1" href="#!" role="button">
                <i class="fab fa-facebook-f"></i></a>

            <!-- Twitter -->
            <a class="btn btn-outline-light btn-floating m-1" href="#!" role="button">
                <i class="fab fa-twitter"></i></a>
                
            <!-- Instagram -->
            <a class="btn btn-outline-light btn-floating m-1" href="#!" role="button">
                <i class="fab fa-instagram"></i></a>
        </section>
        <!-- Section: Social media -->
    </div>
    <!-- Copyright -->
    <div class="text-center p-3" style="background-color: rgba(0, 0, 0, 0.2);">
        © 2023 Copyright:
        <a class="text-white">MDX University</a>
    </div>
    <!-- Copyright -->
</footer>
</template>

<script>

export default {
  name: 'App',
}
</script>

<style>
/* import styles from main.css */
@import './main.css';
</style>
