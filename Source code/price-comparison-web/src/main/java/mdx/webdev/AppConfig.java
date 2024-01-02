package mdx.webdev;

import org.springframework.context.annotation.*;

/**
 * Configuration class for the Spring application context.
 */
@Configuration
public class AppConfig {

    /**
     * Configuration method for the Hibernate session bean.
     *
     * @return An initialized instance of HibernateSession.
     */
    @Bean
    public HibernateSession hibernateSession() {
        HibernateSession hibernateSession = new HibernateSession();
        hibernateSession.init();
        return hibernateSession;
    }

    /**
     * Configuration method for the WebScraperManager bean.
     *
     * @return An instance of WebScraperManager with dependencies set for
     * WebScraper instances.
     */
    @Bean
    public WebScraperManager myScraperManager() {
        //create an instance of WebScraperManager
        WebScraperManager tmpScraperManager = new WebScraperManager();
        //set dependencies (Scraper instances) for WebScraperManager
        tmpScraperManager.setScraper1(myScraper1());
        tmpScraperManager.setScraper2(myScraper2());
        tmpScraperManager.setScraper3(myScraper3());
        tmpScraperManager.setScraper4(myScraper4());
        tmpScraperManager.setScraper5(myScraper5());
        return tmpScraperManager;
    }

    /**
     * Configuration method for the WebScraper1 bean.
     *
     * @return An instance of WebScraper1.
     */
    @Bean
    public WebScraper1 myScraper1() {
        WebScraper1 tmpScraper1 = new WebScraper1();
        return tmpScraper1;
    }

    /**
     * Configuration method for the WebScraper2 bean.
     *
     * @return An instance of WebScraper2.
     */
    @Bean
    public WebScraper2 myScraper2() {
        WebScraper2 tmpScraper2 = new WebScraper2();
        return tmpScraper2;
    }

    /**
     * Configuration method for the WebScraper3 bean.
     *
     * @return An instance of WebScraper3.
     */
    @Bean
    public WebScraper3 myScraper3() {
        WebScraper3 tmpScraper3 = new WebScraper3();
        return tmpScraper3;
    }

    /**
     * Configuration method for the WebScraper4 bean.
     *
     * @return An instance of WebScraper4.
     */
    @Bean
    public WebScraper4 myScraper4() {
        WebScraper4 tmpScraper4 = new WebScraper4();
        return tmpScraper4;
    }

    /**
     * Configuration method for the WebScraper5 bean.
     *
     * @return An instance of WebScraper5.
     */
    @Bean
    public WebScraper5 myScraper5() {
        WebScraper5 tmpScraper5 = new WebScraper5();
        return tmpScraper5;
    }
}
