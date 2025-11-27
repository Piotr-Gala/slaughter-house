package via.pro3.slaughterhouse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import via.pro3.slaughterhouse.domain.Animal;
import via.pro3.slaughterhouse.domain.Part;
import via.pro3.slaughterhouse.domain.Product;
import via.pro3.slaughterhouse.repository.AnimalRepository;
import via.pro3.slaughterhouse.repository.PartRepository;
import via.pro3.slaughterhouse.repository.ProductRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class PackagingServiceTest {

    @Autowired
    PartRepository partRepo;

    @Autowired
    ProductRepository productRepo;

    @Autowired
    AnimalRepository animalRepo;

    PackagingService service;

    @BeforeEach
    void setup() {
        // param order: productRepo, partRepo, requiredTypes CSV
        service = new PackagingService(productRepo, partRepo,
                "leg,rib,loin,shoulder,belly");
    }


    private Part createPart(String type, double weight) {
        Animal a = animalRepo.save(new Animal());
        Part p = new Part();
        p.setType(type);
        p.setWeight(weight);
        p.setAnimal(a);
        return partRepo.save(p);
    }

    // 1) HAPPY PATH: SAME TYPE PRODUCT
    @Test
    void createSameTypeProduct_ok() {
        Part p1 = createPart("leg", 5);
        Part p2 = createPart("leg", 7);

        Product product = service.createSameTypeProduct(List.of(p1.getId(), p2.getId()));

        assertThat(product.getParts()).hasSize(2);
        assertThat(product.getKind().name()).isEqualTo("SAME_TYPE");
    }

    // 2) ERROR: TYPES DIFFERENT
    @Test
    void createSameTypeProduct_invalidTypes() {
        Part p1 = createPart("leg", 5);
        Part p2 = createPart("rib", 7);

        assertThatThrownBy(() ->
                service.createSameTypeProduct(List.of(p1.getId(), p2.getId()))
        ).isInstanceOf(IllegalStateException.class);
    }


    // 3) HAPPY PATH: HALF ANIMAL PRODUCT
    @Test
    void createHalfAnimalProduct_ok() {
        Part leg = createPart("leg", 5);
        Part rib = createPart("rib", 6);
        Part loin = createPart("loin", 6);
        Part shoulder = createPart("shoulder", 7);
        Part belly = createPart("belly", 4);

        Product product = service.createHalfAnimalProduct(
                List.of(leg.getId(), rib.getId(), loin.getId(), shoulder.getId(), belly.getId())
        );

        assertThat(product.getParts()).hasSize(5);
        assertThat(product.getKind().name()).isEqualTo("HALF_ANIMAL");
    }


    // 4) ERROR: MISSING REQUIRED TYPE
    @Test
    void createHalfAnimalProduct_missingType() {
        Part leg = createPart("leg", 5);
        Part rib = createPart("rib", 6);

        assertThatThrownBy(() ->
                service.createHalfAnimalProduct(List.of(leg.getId(), rib.getId()))
        ).isInstanceOf(IllegalStateException.class);
    }
}
