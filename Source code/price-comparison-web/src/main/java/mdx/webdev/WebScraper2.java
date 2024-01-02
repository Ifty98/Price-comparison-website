package mdx.webdev;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.io.IOException;
import org.jsoup.HttpStatusException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * This class represents a web scraper that extracts data from multiple URLs
 * related to watches and stores the information in a database.
 * Implements the Runnable interface for multi-threading.
 */
public class WebScraper2 implements Runnable {

    /**
     * Runs the web scraping process, extracting watch-related data from specified URLs.
     * Connects to each URL, retrieves product information, and stores it in a database using Hibernate.
     * Pauses for 2 seconds between each product extraction.
     */
    @Override
    public void run() {
        //array list of urls from where the data will be scrapped 
        ArrayList<String> urlList = new ArrayList<>();
        urlList.add("https://www.watchshop.com/watches/casio/mens.plp?show=96");
        urlList.add("https://www.watchshop.com/watches/seiko/mens.plp?show=96");
        urlList.add("https://www.watchshop.com/watches/guess.plp?gender[]=mens&show=96");
        //initialize the hibernate session
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        HibernateSession hibernateSession = (HibernateSession) context.getBean("hibernateSession");

        try {
            //connect to each url in the array list
            for (int i = 0; i < urlList.size(); i++) {
                Document doc = Jsoup.connect(urlList.get(i)).get();
                //get the list of products to scrape data of each product 
                Elements productsArea = doc.getElementsByClass("product-container");
                Elements productList = productsArea.select("div.product-img");

                for (int k = 0; k < productList.size(); k++) {
                    //create a watch object for each product to store their data
                    Watch watch = new Watch();
                    Specifications spec = new Specifications();
                    Website web = new Website();
                    //get the link for the selected product 
                    Element product = productList.get(k);
                    Elements linkContainer = product.select("a");
                    Element linkArea = linkContainer.get(0);
                    String href = linkArea.attr("href");
                    String link = "https://www.watchshop.com" + href;
                    web.setWebUrl(link);

                    Document doc2 = Jsoup.connect(link).get();
                    //select the website name where the produtc is located and the product name
                    Elements nameArea = doc2.getElementsByClass("product-single-info");
                    Elements nameSpace = nameArea.select("h1");
                    String productName = nameSpace.get(0).text();
                    String webName = "Watch Shop";
                    web.setWebName(webName);
                    watch.setWatchName(productName);
                    //select the table with the product specifications
                    Elements div = doc2.getElementsByClass("table");
                    Elements body = div.select("tbody");
                    Element tbody = body.get(0);
                    Elements ths = tbody.select("th");
                    Elements tds = tbody.select("td");
                    for (int z = 0; z < ths.size(); z++) {
                        String label = ths.get(z).text();
                        if (label.equals("Brand")) {
                            //store the brand
                            String brand = tds.get(z).text();
                            watch.setBrand(brand);
                        }
                        if (label.equals("Model Name")) {
                            //store the model number
                            String model = tds.get(z).text();
                            spec.setModel(model);
                        }
                        if (label.equals("Strap colour")) {
                            //store the colour
                            String colour = tds.get(z).text();
                            spec.setColour(colour);
                        }
                    }
                    //store the current price 
                    Elements priceArea = doc2.getElementsByClass("product-price");
                    Elements priceElements = priceArea.select("div[class*=product-price] span.price-black, div[class*=product-price] span.sale strong");
                    if (!priceElements.isEmpty()) {
                        Element priceElement = priceElements.first();
                        String priceValue = priceElement.text();
                        String priceVal = priceValue.replaceAll("[^0-9.]", "");
                        float price = Float.parseFloat(priceVal);
                        web.setPrice(price);
                    } else {
                        System.out.println("Price not found");
                    }
                    //select the image of the product and store it
                    Elements imageContainers = doc2.getElementsByClass("product-slider__main");
                    Elements imgs = imageContainers.select("img");
                    String imgSrc = imgs.get(0).attr("src");
                    spec.setImgUrl(imgSrc);
                    //get the description 
                    Elements descriptionArea = doc2.select("div#details");
                    Elements descriptionString = descriptionArea.select("p");
                    String description = descriptionString.get(0).text();
                    watch.setDescription(description);

                    spec.setWebsite(web);
                    watch.setSpecifications(spec);
                    //add the created watch object to the database using hibernate
                    hibernateSession.addWatchIfNotExists(watch);

                    try {
                        //each time a watch data is scraped, stop it for 2 seconds before getting the next product
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {

                    }

                }

            }
        } catch (HttpStatusException e) {
            if (e.getStatusCode() == 500) {
                // Handle the 500 error, log it, or take appropriate action
                System.err.println("HTTP 500 Error: " + e.getMessage());
            } else {
                // Handle other HTTP errors as needed
                System.err.println("HTTP Error: " + e.getMessage());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //Shut down Hibernate
        hibernateSession.shutDown();

    }
}
