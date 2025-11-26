package via.pro3.slaughterhouse.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

// Animal entity representing an animal brought to the slaughterhouse (station 1)
@Entity
public class Animal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private double weight; // kg

    private LocalDate arrivalDate;

    private String origin; // e.g. farm name

    public  Long getId() {
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
    public LocalDate getArrivalDate() { return arrivalDate; }
    public void setArrivalDate(LocalDate arrivalDate) { this.arrivalDate = arrivalDate; }
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

}
