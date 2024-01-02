let express = require('express');
let url = require('url');
const bodyParser = require('body-parser');
//custom module with http status codes
require('./http_status.js');

let app = express();
let mysql = require('mysql2');

//create a connection pool to connect to the MySQL database
let connectionPool = mysql.createPool({
  connectionLimit: 50,
  host: "localhost",
  user: "root",
  password: "Iftynumber2",
  database: "web_scraper",
  debug: false
})

//set up to allow access to requests from localhost:8080
app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', 'http://localhost:8080');
  res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');
  res.header('Access-Control-Allow-Headers', 'Content-Type');
  next();
});

app.use(bodyParser.urlencoded({ extended: false }));
//application listening on localhost port 8082
app.listen(8082);

//get request that returns watches with similar names as the search input grouped by model
app.get('*/search', (req, res) => {
  const searchTerm = req.query.watch_name;
  //get the id of the watches with similar names a the searched watch
  const query = "SELECT specifications_id FROM watch WHERE watch_name LIKE ?";
  //SQL command to get the specifications of the selected watch
  const modelQuery = `
  SELECT specifications.model, specifications.img_url, watch.watch_name FROM specifications 
  JOIN watch ON specifications.specifications_id = watch.specifications_id 
  WHERE specifications.specifications_id = ?`;

  //use the connection pool to query the database
  connectionPool.query(query, [`%${searchTerm}%`], (error, results) => {
    if (error) {
      //handle any database errors
      console.error(error);
      return res.status(HTTP_INTERNAL_SERVER_ERROR).json({ error: 'Database error' });
    }

    //extract the specIDs from the results
    const specIds = results.map(result => result.specifications_id);
    const watchesByModels = [];
    //recursive function to order by model all the watches found 
    function getModel(index) {
      if (index < specIds.length) {
        const specId = specIds[index];
        connectionPool.query(modelQuery, [specId], (error, modelResults) => {
          if (error) {
            //handle any database errors
            console.error(error);
            return res.status(HTTP_INTERNAL_SERVER_ERROR).json({ error: 'Database error' });
          }
          const watchModel = modelResults[0].model;
          const watchImg = modelResults[0].img_url;
          const watchName = modelResults[0].watch_name;
          //check if the model already exists in the response
          const existingModel = watchesByModels.find(item => item.model === watchModel);
          if (existingModel) {
            existingModel.watches.push(specId);
          } else {
            watchesByModels.push({ model: watchModel, img_url: watchImg, watch_name: watchName, watches: [specId] });
          }
          //call the function recursively for the next specID
          getModel(index + 1);
        });
      } else {
        //send the response with watches grouped by models
        res.json({ watchesByModels });
      }
    }

    getModel(0);
  });
});

//get request that retrieves data about watches from a list of specifications ids
app.get('*/getSpecInfo', (req, res) => {
  //extract the specification IDs from the request query parameter
  const specIds = req.query.specIds;
  const specIdsArray = specIds.split(',').map(id => id.trim());
  //SQL command to retrieve detailed information for a watch based on its specification ID
  const query = `
    SELECT specifications.colour, specifications.img_url, specifications.website_id,
    specifications.model, specifications.img_url, watch.watch_name,
    watch.description, watch.brand
    FROM specifications
    JOIN watch ON specifications.specifications_id = watch.specifications_id
    WHERE specifications.specifications_id = ?`;
  //array to store watches grouped by colours
  const watchesByColours = [];
  //recursive function to fetch watch details based on specification IDs
  function getColour(index) {
    if (index < specIdsArray.length) {
      const specId = specIdsArray[index];
      connectionPool.query(query, [specId], (error, colourResults) => {
        if (error) {
          //handle any database errors
          console.error(error);
          return res.status(HTTP_INTERNAL_SERVER_ERROR).json({ error: 'Database error' });
        }
        //extract information for the watch based on the specification ID
        const watchColour = colourResults[0].colour;
        const watchImg = colourResults[0].img_url;
        const webId = colourResults[0].website_id;
        const watchModel = colourResults[0].model;
        const watchName = colourResults[0].watch_name;
        const watchDescription = colourResults[0].description;
        const watchBrand = colourResults[0].brand;
        //check if the colour already exists in the response
        const existingColour = watchesByColours.find(item => item.colour === watchColour);

        if (existingColour) {
          existingColour.watches.push(webId);
        } else {
          //if the colour doesn't exist, create a new entry in the response array with the new colour
          watchesByColours.push({ colour: watchColour, image: watchImg, model: watchModel, name: watchName, description: watchDescription, brand: watchBrand, watches: [webId] });
        }

        getColour(index + 1);
      });
    } else {
      //send the response with watches grouped by colours
      res.json({ watchesByColours });
    }
  }

  getColour(0);
});

//get request that retrieves information for a list of website IDs based on their colours
app.get('*/getInfoByColour', (req, res) => {
  //extract the website IDs from the request query parameter
  const webIds = req.query.webIds;
  const webIdsArray = webIds.split(',').map(id => id.trim());
  //SQL command to get information for a website based on its ID
  const query = `SELECT web_name, web_url, price FROM website WHERE website_id = ?`;
  //array to store website information
  const webInfo = [];
  //recursive function to fetch website information based on website IDs
  function getWebInfo(index) {
    if (index < webIdsArray.length) {
      const webId = webIdsArray[index];
      connectionPool.query(query, [webId], (error, webResults) => {
        if (error) {
          //handle any database errors
          console.error(error);
          return res.status(HTTP_INTERNAL_SERVER_ERROR).json({ error: 'Database error' });
        }
        //extract data for the website based on the website ID
        const webName = webResults[0].web_name;
        const webUrl = webResults[0].web_url;
        const watchprice = webResults[0].price;
        //add the website information to the array
        webInfo.push({ name: webName, url: webUrl, price: watchprice });
        getWebInfo(index + 1);
      });
    } else {
      //send the response with website information
      res.json({ webInfo });
    }
  }

  getWebInfo(0);
});
