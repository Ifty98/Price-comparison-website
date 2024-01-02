const chai = require('chai');
const http = require('http');

const { expect } = chai;
//test case for the search function to get watches ordered by models
describe('GET /search', () => {
  it('should return watches grouped by models for a valid search input', (done) => {
    const searchTerm = 'casio';

    const options = {
      hostname: 'localhost',
      port: 8082,
      path: `/search?watch_name=${searchTerm}`,
      method: 'GET',
    };
    //create http request for testing
    const req = http.request(options, (res) => {
      let data = '';

      res.on('data', (chunk) => {
        data += chunk;
      });

      res.on('end', () => {
        const parsedData = JSON.parse(data);
        //this should be the expected results from the response
        expect(res.statusCode).to.equal(200);
        expect(parsedData).to.have.property('watchesByModels');
        expect(parsedData.watchesByModels).to.be.an('array');

        done();
      });
    });
    //handle any errors in the test case
    req.on('error', (err) => {
      done(err);
    });

    req.end();
  });
});

//test case for function to get watches ordered by colours
describe('GET /getSpecInfo', () => {
  it('should return watches grouped by colours for valid specification IDs', (done) => {
    const specIds = '1,2,3';

    const options = {
      hostname: 'localhost',
      port: 8082,
      path: `/getSpecInfo?specIds=${specIds}`,
      method: 'GET',
    };
    //create http request for testing
    const req = http.request(options, (res) => {
      let data = '';

      res.on('data', (chunk) => {
        data += chunk;
      });

      res.on('end', () => {
        const parsedData = JSON.parse(data);
        //this should be the expected results from the response
        expect(res.statusCode).to.equal(200);
        expect(parsedData).to.have.property('watchesByColours');
        expect(parsedData.watchesByColours).to.be.an('array');

        done();
      });
    });
    //handle any error during the request
    req.on('error', (err) => {
      done(err);
    });

    req.end();
  });
});

//test case for function to get websites data about the selected watch
describe('GET /getInfoByColour', () => {
  it('should return website information for valid website IDs', (done) => {
    const webIds = '1,2,3';

    const options = {
      hostname: 'localhost',
      port: 8082,
      path: `/getInfoByColour?webIds=${webIds}`,
      method: 'GET',
    };
    //create http request for testing
    const req = http.request(options, (res) => {
      let data = '';

      res.on('data', (chunk) => {
        data += chunk;
      });

      res.on('end', () => {
        const parsedData = JSON.parse(data);
        //this should be the expected results from the response
        expect(res.statusCode).to.equal(200);
        expect(parsedData).to.have.property('webInfo');
        expect(parsedData.webInfo).to.be.an('array');

        done();
      });
    });
    //handle any error during the request
    req.on('error', (err) => {
      done(err);
    });

    req.end();
  });
});
