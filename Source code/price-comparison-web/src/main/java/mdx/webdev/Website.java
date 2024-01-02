package mdx.webdev;

import javax.persistence.*;

/**
 * This class represents a website and is annotated as an Entity for JPA.
 */
@Entity
@Table(name = "website")
public class Website {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "website_id")
    private int id;

    @Column(name = "web_name", nullable = false, length = 50, columnDefinition = "VARCHAR(50)")
    private String webName;

    @Column(name = "web_url", nullable = false, columnDefinition = "TEXT")
    private String webUrl;

    @Column(name = "price", columnDefinition = "FLOAT DEFAULT 0")
    private Float price;

    /**
     * Default constructor for Website. Initializes default values.
     */
    public Website() {
        this.webName = "No name";
        this.webUrl = "No url";
        this.price = 0.00f;
    }

    /**
     * Gets the ID of the website.
     *
     * @return The ID of the website.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the website.
     *
     * @param id The ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the website.
     *
     * @return The name of the website.
     */
    public String getWebName() {
        return webName;
    }

    /**
     * Sets the name of the website.
     *
     * @param webName The name to set.
     */
    public void setWebName(String webName) {
        this.webName = webName;
    }

    /**
     * Gets the URL of the website.
     *
     * @return The URL of the website.
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * Sets the URL of the website.
     *
     * @param webUrl The URL to set.
     */
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    /**
     * Gets the price of the website.
     *
     * @return The price of the website.
     */
    public Float getPrice() {
        return price;
    }

    /**
     * Sets the price of the website.
     *
     * @param price The price to set.
     */
    public void setPrice(Float price) {
        this.price = price;
    }
}
