package mdx.webdev;

import javax.persistence.*;

/**
 * This class represents the specifications of a watch and is annotated as an
 * Entity for JPA.
 */
@Entity
@Table(name = "specifications")
public class Specifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specifications_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "website_id")
    private Website website;

    @Column(name = "model", nullable = false, length = 50, columnDefinition = "VARCHAR(50) DEFAULT '0'")
    private String model;

    @Column(name = "colour", nullable = false, length = 50, columnDefinition = "VARCHAR(50) DEFAULT '0'")
    private String colour;

    @Column(name = "img_url", columnDefinition = "TEXT")
    private String imgUrl;

    /**
     * Default constructor for Specifications
     */
    public Specifications() {
        this.model = "No model";
        this.colour = "No colour";
        this.imgUrl = "No image";
    }

    /**
     * Gets the ID of the specifications.
     *
     * @return The ID of the specifications.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the specifications.
     *
     * @param id The ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the associated website of the specifications.
     *
     * @return The associated website of the specifications.
     */
    public Website getWebsite() {
        return website;
    }

    /**
     * Sets the associated website of the specifications.
     *
     * @param website The website to set.
     */
    public void setWebsite(Website website) {
        this.website = website;
    }

    /**
     * Gets the model of the specifications.
     *
     * @return The model of the specifications.
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model of the specifications.
     *
     * @param model The model to set.
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets the colour of the specifications.
     *
     * @return The colour of the specifications.
     */
    public String getColour() {
        return colour;
    }

    /**
     * Sets the colour of the specifications.
     *
     * @param colour The colour to set.
     */
    public void setColour(String colour) {
        this.colour = colour;
    }

    /**
     * Gets the image URL of the specifications.
     *
     * @return The image URL of the specifications.
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * Sets the image URL of the specifications.
     *
     * @param imgUrl The image URL to set.
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
