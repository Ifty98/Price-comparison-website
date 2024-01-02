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
public class WebScraper3 implements Runnable {

    /**
     * Runs the web scraping process, extracting watch-related data from specified URLs.
     * Connects to each URL, retrieves product information, and stores it in a database using Hibernate.
     * Pauses for 2 seconds between each product extraction.
     */
    @Override
    public void run() {
        //array list of urls from where the data will be scrapped 
        ArrayList<String> urlList = new ArrayList<>();
        urlList.add("https://www.houseofwatches.co.uk/casio-watches/filter/gender-mens/?product_list_limit=48");
        urlList.add("https://www.houseofwatches.co.uk/casio-watches/filter/gender-mens/?p=2&product_list_limit=48");
        urlList.add("https://www.houseofwatches.co.uk/guess-watches/filter/gender-mens/?product_list_limit=48");
        urlList.add("https://www.houseofwatches.co.uk/seiko-watches/filter/gender-mens/?product_list_limit=48");
        urlList.add("https://www.houseofwatches.co.uk/seiko-watches/filter/gender-mens/?p=2&product_list_limit=48");
        //initialize the hibernate session
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        HibernateSession hibernateSession = (HibernateSession) context.getBean("hibernateSession");

        try {
            //connect to each url in the array list
            for (int i = 0; i < urlList.size(); i++) {
                Document doc = Jsoup.connect(urlList.get(i)).get();
                //get the list of products to scrape data of each product
                Elements productsArea = doc.getElementsByClass("catalog-grid");
                Elements productsList = productsArea.select("li");

                for (int k = 0; k < productsList.size(); k++) {
                    //create a watch object for each product to store their data
                    Watch watch = new Watch();
                    Specifications spec = new Specifications();
                    Website web = new Website();
                    //get the link for the selected product 
                    Element product = productsList.get(k);
                    Elements linkContainer = product.select("a");
                    Element linkArea = linkContainer.get(0);
                    String link = linkArea.attr("href");
                    web.setWebUrl(link);

                    Document doc2 = Jsoup.connect(link).get();
                    //select the website name where the produtc is located and the product name
                    Elements nameArea = doc2.getElementsByClass("product-view__description");
                    Elements nameSpace = nameArea.select("span");
                    String productName = nameSpace.get(0).text();
                    String webName = "House of Watches";
                    web.setWebName(webName);
                    watch.setWatchName(productName);
                    //select the table with the product specifications
                    Elements div = doc2.getElementsByClass("product-details-specification");
                    Elements tables = div.select("table");
                    Element firstTable = tables.get(0);
                    Elements firstTrs = firstTable.select("tr");

                    for (int z = 0; z < firstTrs.size(); z++) {
                        Element tr = firstTrs.get(z);
                        Elements tds = tr.select("td");
                        for (int g = 0; g < tds.size(); g++) {
                            String label = tds.get(g).text();
                            if (label.equals("Brand")) {
                                //store the brand
                                String brand = tds.get(g + 1).text();
                                watch.setBrand(brand);
                            }
                            if (label.equals("Brand Code")) {
                                //store the model number
                                String model = tds.get(g + 1).text();
                                spec.setModel(model);
                            }
                        }

                    }
                    //store the colour
                    Element secondTable = tables.get(1);
                    Elements secondTrs = secondTable.select("tr");
                    Element tr = secondTrs.get(0);
                    Elements tds = tr.select("td");
                    for (int z = 0; z < tds.size(); z++) {
                        String label = tds.get(z).text();
                        if (label.equals("Strap Colour")) {
                            String colour = tds.get(z + 1).text();
                            spec.setColour(colour);
                        }
                    }
                    //store the current price 
                    Elements priceArea = doc2.getElementsByClass("product-view__price");
                    Elements priceSpace = priceArea.select("span");
                    String priceString = priceSpace.get(0).text();
                    String priceValue = priceString.replaceAll("[^0-9.]", "");
                    float price = Float.parseFloat(priceValue);
                    web.setPrice(price);
                    //select the image of the product and store it
                    Elements imageContainers = doc2.getElementsByClass("main-gallery");
                    Elements imgs = imageContainers.select("img");
                    String imgSrc = imgs.get(0).attr("src");
                    spec.setImgUrl(imgSrc);
                    //get the description 
                    Elements descriptionArea = doc2.getElementsByClass("product-details-text");
                    String description = descriptionArea.text();
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
