package via.pro3.slaughterhouse.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;

@Entity
public class Part {
    @Id @UuidGenerator
    private UUID id;

    @Column(nullable=false)
    private double weight; // kg

    @Column(nullable=false)
    private String type;

    @ManyToOne(optional=false) @JoinColumn(name="animal_id")
    private Animal animal;

    @ManyToOne @JoinColumn(name="tray_id")
    private Tray tray;

    // getters/setters
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Animal getAnimal() {
        return animal;
    }
    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
    public Tray getTray() {
        return tray;
    }
    public void setTray(Tray tray) {
        this.tray = tray;
    }
}
