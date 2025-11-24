package via.pro3.slaughterhouse.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import java.util.*;

// Product entity representing a final product made from one or more parts (station 3)
@Entity
public class Product {
    @Id @UuidGenerator
    private UUID id;

    @Column(nullable=false)
    private ProductKind kind; // SAME_TYPE | HALF_ANIMAL

    // join table product_part(product_id, part_id)
    @ManyToMany
    @JoinTable(
            name = "product_part",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "part_id")
    )
    private Set<Part> parts = new HashSet<>();

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public ProductKind getKind() {
        return kind;
    }
    public void setKind(ProductKind kind) {
        this.kind = kind;
    }
    public Set<Part> getParts() {
        return parts;
    }
    public void setParts(Set<Part> parts) {
        this.parts = parts;
    }

}
