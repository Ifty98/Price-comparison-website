<template>
    <div class="container mt-5 pt-5 pb-5 search-box">
        <div class="row">
            <!-- display search results information -->
            <h2>You searched for the following watch " {{ $route.params.watchName }} "</h2>
            <p>Select the watch you are looking for</p>
        </div>
    </div>
    <!-- container for displaying search results -->
    <div class="container mt-5 pt-5 pb-5">
        <div class="row">
            <!-- display watches using pagination -->
            <div class="col-2 pt-5" v-for="(watch, index) in itemsInPage" v-bind:key="index">
                <div class="card watch-display">
                    <!-- watch image -->
                    <img :src="watch.img_url" class="card-img-top" alt="watch image">
                    <a href="" v-on:click="findProduct(watch)">
                        <!-- watch details in card body -->
                        <div class="card-body">
                            <h6 class="card-title">{{ shortName(watch.watch_name) }}</h6>
                            <div class="card-footer">
                                <h6 class="card-text"><b>Model</b>: {{ watch.model }} </h6>
                            </div>
                        </div>
                    </a>
                </div>
            </div>
        </div>
    </div>
    <!-- pagination section -->
    <div class="d-flex justify-content-center">
        <nav aria-label="Page navigation example">
            <ul class="pagination">
                <!-- previous page button -->
                <li class="page-item">
                    <a class="page-link" href="#" aria-label="Previous" v-on:click="getPageData(actualPage - 1)">
                        <span aria-hidden="true">&laquo;</span>
                        <span class="sr-only">Previous</span>
                    </a>
                </li>
                <!-- total pages -->
                <li class="page-item" v-for="page in totalPages()" v-bind:key="page"
                    :class="{ 'active': actualPage === page }" v-on:click="getPageData(page)"><a class="page-link" href="#">
                        {{ page }} </a></li>
                <li class="page-item">
                    <!-- next page button -->
                    <a class="page-link" href="#" aria-label="Next" v-on:click="getPageData(actualPage + 1)">
                        <span aria-hidden="true">&raquo;</span>
                        <span class="sr-only">Next</span>
                    </a>
                </li>
            </ul>
        </nav>
    </div>
</template>

<script>

import axios from 'axios';

export default {
    name: 'ResultsPage',

    data() {
        return {
            //array to store watches grouped by models
            watchesByModels: [

            ],
            watchesPerPage: 12,
            //array to store watches for the current page
            itemsInPage: [

            ],
            actualPage: 1,
        };
    },

    mounted() {
        //run the search function when the component is mounted
        this.search();
    },

    methods: {
        //calculate the total number of pages based on the watches available
        totalPages() {
            return Math.ceil(this.watchesByModels.length / this.watchesPerPage);
        },
        
        //get watches for the specified page
        getPageData(pageNumber) {
            this.itemsInPage = [];
            const start = (pageNumber - 1) * this.watchesPerPage;
            const end = start + this.watchesPerPage;
            this.itemsInPage = this.watchesByModels.slice(start, end);
            this.actualPage = pageNumber;
        },
        
        //make a request to the server to find watches with similar name as the user input
        search() {
            axios.get('http://localhost:8082/search?watch_name=' + this.$route.params.watchName)
                .then(response => {
                    //store the response 
                    this.watchesByModels = response.data.watchesByModels;
                    //display watches for the first page
                    this.getPageData(1);
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        },
        
        //method to cut the watch name if it is too long
        shortName(name) {
            const maxLength = 30;
            return name.length > maxLength ? name.slice(0, maxLength) + '...' : name;
        },

        //navigate to the detailed product page for a specific watch
        findProduct(watch) {
            //encode watch IDs and navigate to the product page
            const encodedIds = encodeURIComponent(JSON.stringify(watch.watches));
            import('@/main').then(({ default: router }) => {
                router.push({ name: 'product', params: { ids: encodedIds }});
            });
        },
    },
};

</script>