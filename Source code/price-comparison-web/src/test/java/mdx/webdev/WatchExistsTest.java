package mdx.webdev;

import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * JUnit test class for the WatchExists class. It tests the watchExists method
 * in the HibernateSession class.
 */
public class WatchExistsTest {

    private HibernateSession hibernateSession;

    /**
     * Set up method executed before each test. Initializes the Hibernate
     * session for testing.
     */
    @Before
    public void setUp() {
        hibernateSession = new HibernateSession();
        hibernateSession.init();
    }

    /**
     * Tear down method executed after each test. Shuts down the Hibernate
     * session after testing.
     */
    @After
    public void tearDown() {
        hibernateSession.shutDown();
    }

    /**
     * Test case for the watchExists method. Verifies that the method correctly
     * determines whether a watch exists in the database.
     */
    @Test
    public void testAddWatchIfNotExists() {
        Watch watch = createWatch();

        //check if the sample watch exists in the database
        assertFalse(hibernateSession.watchExists(watch.getBrand(), watch.getSpecifications().getModel(),
                watch.getSpecifications().getColour(), watch.getSpecifications().getWebsite().getWebName()));

        //if the test reaches this point without failures, then [rint a message
        System.out.println("Check if the watch exists in the database or not method test passed!!");
    }

    /**
     * Creates a sample Watch object for testing purposes.
     *
     * @return The created sample Watch object.
     */
    private Watch createWatch() {
        // Create a sample Watch for testing
        Watch watch = new Watch();
        watch.setBrand("tempBrand");
        watch.setDescription("tempDescription");
        watch.setWatchName("tempWatch");

        Specifications specifications = new Specifications();
        specifications.setModel("tempModel");
        specifications.setColour("tempColour");

        Website website = new Website();
        website.setWebName("tempWebName");
        specifications.setWebsite(website);

        watch.setSpecifications(specifications);

        return watch;
    }
}
