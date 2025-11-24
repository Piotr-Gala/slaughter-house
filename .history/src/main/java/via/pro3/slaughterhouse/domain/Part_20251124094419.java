package via.pro3.slaughterhouse.domain;

import jakarta.persistence.*;

// Part entity representing a butchered PART of an animal (put on trays in station 2)
@Entity
public class Part {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private double weight; // kg

    @Column(nullable=false)
    private String type;

    @ManyToOne(optional=false) @JoinColumn(name="animal_id")
    private Animal animal;

    @ManyToOne @JoinColumn(name="tray_id")
    private Tray tray;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
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
