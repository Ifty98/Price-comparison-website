package mdx.webdev;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * The main class for starting the web scraping process from multiple websites
 * using multi-threading.
 */
public class App {
    /**
     * The main method to start the application.
     *
     * @param args The command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        WebScraperManager scraperManager = (WebScraperManager) context.getBean("myScraperManager");

        //start the web scraping process
        scraperManager.startScraping();
    }
}
