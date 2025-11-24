package via.pro3.slaughterhouse.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

// Animal entity representing an animal brought to the slaughterhouse (station 1)
@Entity
public class Animal {
    @Id @UuidGenerator
    private UUID id;

    @Column(nullable=false, unique=true)
    private String registrationNumber;

    @Column(nullable=false)
    private double weight; // kg

    private LocalDate arrivalDate;

    private String origin; // e.g. farm name

    public  UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getRegistrationNumber() {
        return registrationNumber;
    }
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
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
