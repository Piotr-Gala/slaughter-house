package via.pro3.slaughterhouse.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import via.pro3.slaughterhouse.domain.Animal;
import via.pro3.slaughterhouse.domain.Part;
import via.pro3.slaughterhouse.domain.Product;
import via.pro3.slaughterhouse.domain.ProductKind;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)

class TraceRepositoryTest {

    @Autowired
    TraceRepository traceRepository;

    @Autowired
    AnimalRepository animalRepository;

    @Autowired
    PartRepository partRepository;

    @Autowired
    ProductRepository productRepository;

    private Product product;
    private Animal animal1;
    private Animal animal2;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        partRepository.deleteAll();
        animalRepository.deleteAll();

        // tworzymy zwierzaki – TERAZ BEZ registrationNumber
        animal1 = new Animal();
        animal1.setWeight(600);
        animal1 = animalRepository.save(animal1);

        animal2 = new Animal();
        animal2.setWeight(580);
        animal2 = animalRepository.save(animal2);

        // części
        Part p1 = new Part();
        p1.setType("leg");
        p1.setWeight(10);
        p1.setAnimal(animal1);
        p1 = partRepository.save(p1);

        Part p2 = new Part();
        p2.setType("rib");
        p2.setWeight(8);
        p2.setAnimal(animal2);
        p2 = partRepository.save(p2);

        // produkt
        product = new Product();
        product.setKind(ProductKind.HALF_ANIMAL);
        product.getParts().add(p1);
        product.getParts().add(p2);
        productRepository.save(product);
    }

    @Test
    void animalsByProduct_ok() {
        List<String> ids = traceRepository.findAnimalIdByProductId(product.getId());

        assertThat(ids)
                .containsExactlyInAnyOrder(
                        animal1.getId().toString(),
                        animal2.getId().toString()
                );
    }

    @Test
    void productsByAnimal_ok() {
        List<Long> productIds = traceRepository.findProductIdsByAnimalId(
                animal1.getId().toString()
        );

        assertThat(productIds)
                .contains(product.getId());
    }
}
