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
public class WebScraper4 implements Runnable {

    /**
     * Runs the web scraping process, extracting watch-related data from specified URLs.
     * Connects to each URL, retrieves product information, and stores it in a database using Hibernate.
     * Pauses for 2 seconds between each product extraction.
     */
    @Override
    public void run() {
        //array list of urls from where the data will be scrapped 
        ArrayList<String> urlList = new ArrayList<>();
        urlList.add("https://www.thewatchhut.co.uk/brands/casio-watches.htm?gender=gents&show=96");
        urlList.add("https://www.thewatchhut.co.uk/brands/seiko-watches.htm?gender=gents&show=96");
        urlList.add("https://www.thewatchhut.co.uk/brands/guess-watches.htm?gender=gents&show=96");
        //initialize the hibernate session
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        HibernateSession hibernateSession = (HibernateSession) context.getBean("hibernateSession");

        try {
            //connect to each url in the array list
            for (int i = 0; i < urlList.size(); i++) {
                Document doc = Jsoup.connect(urlList.get(i)).get();
                //get the list of products to scrape data of each product
                Elements productsArea = doc.getElementsByClass("match");

                for (int k = 0; k < productsArea.size(); k++) {
                    //create a watch object for each product to store their data
                    Watch watch = new Watch();
                    Specifications spec = new Specifications();
                    Website web = new Website();
                    //get the link for the selected product 
                    Element product = productsArea.get(k);
                    Elements linkContainer = product.select("a");
                    Element linkArea = linkContainer.get(0);
                    String href = linkArea.attr("href");
                    String link = "https://www.thewatchhut.co.uk" + href;
                    web.setWebUrl(link);

                    Document doc2 = Jsoup.connect(link).get();
                    //select the website name where the produtc is located and the product name
                    Elements nameArea = doc2.getElementsByClass("productDetails");
                    Elements nameSpace = nameArea.select("h4");
                    String productName = nameSpace.get(0).text();
                    String webName = "The Watch Hut";
                    web.setWebName(webName);
                    watch.setWatchName(productName);
                    //select the table with the product specifications
                    Elements container = doc2.getElementsByClass("tab-content");
                    Elements div = container.select("table");
                    Elements trs = div.select("tr");
                    for (int z = 0; z < trs.size(); z++) {
                        Element tr = trs.get(z);
                        Elements ths = tr.select("th");
                        Elements tds = tr.select("td");
                        String label = ths.get(0).text();
                        if (label.equals("Brand")) {
                            //store the brand
                            String brand = tds.get(0).text();
                            watch.setBrand(brand);
                        }
                        if (label.equals("MPN")) {
                            //store the model number
                            String model = tds.get(0).text();
                            spec.setModel(model);
                        }
                        if (label.equals("Strap colour")) {
                            //store the colour
                            String colour = tds.get(0).text();
                            spec.setColour(colour);
                        }
                    }
                    //store the current price 
                    Elements priceArea = doc2.getElementsByClass("pricing");
                    Elements priceDiv = priceArea.select("div.simpleCurrentPrice");
                    String priceString = priceDiv.get(0).text();
                    String priceValue = priceString.replaceAll("[^0-9.]", "");
                    float price = Float.parseFloat(priceValue);
                    web.setPrice(price);                     
                    //select the image of the product and store it
                    Elements imgArea = doc2.getElementsByClass("product-main-image");
                    Elements imgContainer = imgArea.select("a");
                    String imgSrc = imgContainer.get(0).attr("href");
                    spec.setImgUrl(imgSrc);
                    //get the description 
                    String description = "No description";
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
