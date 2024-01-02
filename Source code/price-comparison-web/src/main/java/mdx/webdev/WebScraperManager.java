package mdx.webdev;

/**
 * Manages multiple web scrapers for parallel execution using threads.
 * Coordinates the initialization and execution of individual WebScraper
 * instances.
 */
public class WebScraperManager {
    //instances of WebScraper classes

    private WebScraper1 scraper1;
    private WebScraper2 scraper2;
    private WebScraper3 scraper3;
    private WebScraper4 scraper4;
    private WebScraper5 scraper5;

    /**
     * Default constructor for WebScraperManager.
     */
    public WebScraperManager() {

    }

    //getters and setters for all web scrapers
    public void setScraper1(WebScraper1 scraper1) {
        this.scraper1 = scraper1;
    }

    public WebScraper1 getScraper1() {
        return scraper1;
    }

    public void setScraper2(WebScraper2 scraper2) {
        this.scraper2 = scraper2;
    }

    public WebScraper2 getScraper2() {
        return scraper2;
    }

    public void setScraper3(WebScraper3 scraper3) {
        this.scraper3 = scraper3;
    }

    public WebScraper3 getScraper3() {
        return scraper3;
    }

    public void setScraper4(WebScraper4 scraper4) {
        this.scraper4 = scraper4;
    }

    public WebScraper4 getScraper4() {
        return scraper4;
    }

    public void setScraper5(WebScraper5 scraper5) {
        this.scraper5 = scraper5;
    }

    public WebScraper5 getScraper5() {
        return scraper5;
    }

    /**
     * Starts the web scraping process using multiple threads. Each thread is
     * responsible for running a specific WebScraper instance.
     */
    public void startScraping() {
        Thread thread1 = new Thread(scraper1);
        Thread thread2 = new Thread(scraper2);
        Thread thread3 = new Thread(scraper3);
        Thread thread4 = new Thread(scraper4);
        Thread thread5 = new Thread(scraper5);
        //start each thread
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
    }
}
