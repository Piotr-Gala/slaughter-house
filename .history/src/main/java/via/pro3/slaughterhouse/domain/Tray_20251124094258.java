package via.pro3.slaughterhouse.domain;

import jakarta.persistence.*;
import java.util.*;

// Tray entity representing a tray used to hold parts in station 2
@Entity
public class Tray {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String type; // e.g. leg, rib

    @Column(nullable=false)
    private double maxWeight;

    @OneToMany(mappedBy = "tray")
    private List<Part> parts = new ArrayList<>();

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public double getMaxWeight() {
        return maxWeight;
    }
    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }
    public List<Part> getParts() {
        return parts;
    }
    public void setParts(List<Part> parts) {
        this.parts = parts;
    }
}
