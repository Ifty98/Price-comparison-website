package mdx.webdev;

import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;

/**
 * This class manages Hibernate sessions and provides methods to interact with
 * the database.
 */
public class HibernateSession {

    //use this class to create new Sessions to interact with the database 
    private SessionFactory sessionFactory;

    /**
     * Empty constructor for HibernateSession.
     */
    HibernateSession() {
    }

    /**
     * Sets up the session factory
     */
    public void init() {
        try {
            //create a builder for the standard service registry
            StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

            //load configuration from hibernate configuration file
            standardServiceRegistryBuilder.configure("/hibernate.cfg.xml");

            //create the registry that will be used to build the session factory
            StandardServiceRegistry registry = standardServiceRegistryBuilder.build();

            try {
                //create the session factory - this is the goal of the init method.
                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                /* the registry would be destroyed by the HibernateSession, 
                        but we had trouble building the HibernateSession, so destroy it manually */
                System.err.println("Session Factory build failed.");
                e.printStackTrace();
                StandardServiceRegistryBuilder.destroy(registry);
            }

            //ouput result
            System.out.println("Session factory built.");

        } catch (Throwable ex) {
            //display message if the session creation failed
            System.err.println("SessionFactory creation failed." + ex);
        }
    }

    /**
     * Closes Hibernate down and stops its threads from running.
     */
    public void shutDown() {
        sessionFactory.close();
    }

    /**
     * Adds a watch to the database.
     *
     * @param watch The watch object to be added.
     */
    public void addWatch(Watch watch) {
        try (Session session = sessionFactory.openSession()) {
            // Start transaction
            session.beginTransaction();
            // Save the watch object, which may include associated Specifications and Website
            session.save(watch.getSpecifications());
            session.save(watch.getSpecifications().getWebsite());
            session.save(watch);
            System.out.println("Watch added to database with ID: " + watch.getId());

            // Commit transaction to save changes to the database
            session.getTransaction().commit();
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        }

    }

    /**
     * Checks if a watch with the specified details already exists in the
     * database.
     *
     * @param brand The brand of the watch.
     * @param model The model of the watch.
     * @param colour The colour of the watch.
     * @param webName The name of the website.
     * @return True if the watch exists; false otherwise.
     */
    public boolean watchExists(String brand, String model, String colour, String webName) {
        try (Session session = sessionFactory.openSession()) {
            //look in the database a watch with the brand, model, colour, and web name same as the selected watch
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Watch w WHERE w.brand = :brand AND w.specifications.model = :model "
                    + "AND w.specifications.colour = :colour "
                    + "AND w.specifications.website.webName = :webName", Long.class)
                    .setParameter("brand", brand)
                    .setParameter("model", model)
                    .setParameter("colour", colour)
                    .setParameter("webName", webName);

            Long count = query.uniqueResult();
            //return true if the watch exists in the database
            return count != null && count > 0;
        } catch (Exception e) {
            //handle any exceptions
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adds a watch to the database if it does not already exist.
     *
     * @param watch The watch object to be added.
     */
    public void addWatchIfNotExists(Watch watch) {
        if (!watchExists(watch.getBrand(), watch.getSpecifications().getModel(), watch.getSpecifications().getColour(),
                watch.getSpecifications().getWebsite().getWebName())) {
            addWatch(watch);
        } else {
            System.out.println("Watch already exists in the database");
        }
    }
}
