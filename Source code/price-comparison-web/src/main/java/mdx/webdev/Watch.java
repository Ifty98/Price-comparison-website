package mdx.webdev;

import javax.persistence.*;

/**
 * This class represents a watch and is annotated as an Entity for JPA.
 */
@Entity
@Table(name = "watch")
public class Watch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "watch_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "specifications_id")
    private Specifications specifications;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "watch_name", nullable = false, columnDefinition = "TEXT")
    private String watchName;

    @Column(name = "brand", nullable = false, length = 50)
    private String brand;

    /**
     * Default constructor for Watch. Initializes default values.
     */
    public Watch() {
        this.description = "No description";
        this.watchName = "No name";
        this.brand = "No brand";
    }

    /**
     * Gets the ID of the watch.
     *
     * @return The ID of the watch.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the watch.
     *
     * @param id The ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the specifications of the watch.
     *
     * @return The specifications of the watch.
     */
    public Specifications getSpecifications() {
        return specifications;
    }

    /**
     * Sets the specifications of the watch.
     *
     * @param specifications The specifications to set.
     */
    public void setSpecifications(Specifications specifications) {
        this.specifications = specifications;
    }

    /**
     * Gets the description of the watch.
     *
     * @return The description of the watch.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the watch.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the name of the watch.
     *
     * @return The name of the watch.
     */
    public String getWatchName() {
        return watchName;
    }

    /**
     * Sets the name of the watch.
     *
     * @param watchName The name to set.
     */
    public void setWatchName(String watchName) {
        this.watchName = watchName;
    }

    /**
     * Gets the brand of the watch.
     *
     * @return The brand of the watch.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the brand of the watch.
     *
     * @param brand The brand to set.
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }
}
