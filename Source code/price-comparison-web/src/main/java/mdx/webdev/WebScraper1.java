package mdx.webdev;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.io.IOException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.jsoup.HttpStatusException;

/**
 * This class represents a web scraper that extracts data from multiple URLs
 * related to watches and stores the information in a database.
 * Implements the Runnable interface for multi-threading.
 */
public class WebScraper1 implements Runnable {

    /**
     * Runs the web scraping process, extracting watch-related data from specified URLs.
     * Connects to each URL, retrieves product information, and stores it in a database using Hibernate.
     * Pauses for 2 seconds between each product extraction.
     */
    @Override
    public void run() {
        //array list of urls from where the data will be scrapped 
        ArrayList<String> urlList = new ArrayList<>();
        urlList.add("https://www.hsamuel.co.uk/watches/brands/seiko/c/5439000000?facetCode=gender_string&q=%3A_relevance_Ascending%3Agender_string%3AMEN&text=&storePickup=&sameDayDelivery=false&gbapiv2=false");
        urlList.add("https://www.hsamuel.co.uk/watches/brands/guess/c/5431300000?facetCode=gender_string&q=%3A_relevance_Ascending%3Agender_string%3AMEN&text=&storePickup=&sameDayDelivery=false&gbapiv2=false");
        urlList.add("https://www.hsamuel.co.uk/watches/brands/casio/c/5435000000?facetCode=gender_string&q=%3A_relevance_Ascending%3Agender_string%3AMEN&text=&storePickup=false&sameDayDelivery=false&gbapiv2=false");
        //initialize the hibernate 
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        HibernateSession hibernateSession = (HibernateSession) context.getBean("hibernateSession");

        try {
            //connect to each url in the array list
            for (int i = 0; i < urlList.size(); i++) {
                Document doc = Jsoup.connect(urlList.get(i)).get();
                //get the list of products to scrape data of each product 
                Elements productsArea = doc.getElementsByClass("product-listing product-grid products-list");
                Elements productList = productsArea.select("li");

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
                    String link = "https://www.hsamuel.co.uk/" + href;
                    web.setWebUrl(link);

                    Document doc2 = Jsoup.connect(link).get();
                    //select the website name where the produtc is located
                    Elements webNameContainer = doc2.getElementsByClass("logo-container");
                    Elements webNameArea = webNameContainer.select("img");
                    String webName = webNameArea.get(0).attr("alt");
                    web.setWebName(webName);
                    //select the product name
                    Elements nameArea = doc2.getElementsByClass("product-detail__name");
                    String productName = nameArea.get(0).text();
                    watch.setWatchName(productName);
                    //select the table with the product specifications
                    Elements div = doc2.getElementsByClass("table table-unstriped specs-table product-cms__specsTable");
                    Elements body = div.select("tbody");
                    Element tbody = body.get(1);
                    Elements tr = tbody.select("tr");
                    //iterate through the specifications table to get the product model, brand and colour
                    for (int z = 0; z < tr.size(); z++) {
                        Element row = tr.get(z);
                        Elements tds = row.select("td");
                        Element firstTd = tds.get(0);
                        String label = firstTd.text();
                        //store the model number
                        if (label.equals("Model Number")) {
                            spec.setModel(tds.get(1).text());
                        }
                        //store the brand
                        if (label.equals("Watch Brand")) {
                            watch.setBrand(tds.get(1).text());
                        }
                        //store the colour
                        if (label.equals("Watch Face Colour")) {
                            spec.setColour(tds.get(1).text());
                        } else {

                        }
                    }
                    //select the image of the product and store it 
                    Elements imgContainers = doc2.getElementsByClass("product-gallery__main-item");
                    Elements imgs = imgContainers.select("img");
                    String imgSrc = imgs.get(0).attr("src");
                    String img = "https://www.hsamuel.co.uk/" + imgSrc;
                    spec.setImgUrl(img);
                    //store the current price 
                    Elements priceArea = doc2.getElementsByClass("product-price__price");
                    String productPrice = priceArea.get(0).text();
                    String priceValue = productPrice.replaceAll("[^0-9.]", "");
                    float price = Float.parseFloat(priceValue);
                    web.setPrice(price);
                    //get the description 
                    Elements descriptionArea = doc2.getElementsByClass("product-cms__description");
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
